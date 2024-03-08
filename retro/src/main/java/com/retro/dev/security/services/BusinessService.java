package com.retro.dev.security.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.retro.dev.dtos.BusinessOffersListDTO;
import com.retro.dev.dtos.BusinessServicesDTO;
import com.retro.dev.models.BusinessServices;
import com.retro.dev.models.Category;
import com.retro.dev.models.ChooseOrder;
import com.retro.dev.models.ERole;
import com.retro.dev.models.EStatus;
import com.retro.dev.models.Review;
import com.retro.dev.models.ServiceRequest;
import com.retro.dev.models.SubCategory;
import com.retro.dev.models.User;
import com.retro.dev.models.UserCategory;
import com.retro.dev.payload.request.BusinessOffersListRequest;
import com.retro.dev.payload.request.BusinessServicesRequest;
import com.retro.dev.payload.response.BusinessOffersListResponse;
import com.retro.dev.payload.response.BusinessServicesResponse;
import com.retro.dev.repository.BusinessServicesRepository;
import com.retro.dev.repository.ChooseOrderRepository;
import com.retro.dev.repository.ReviewRepository;
import com.retro.dev.repository.ServiceRequestRepository;
import com.retro.dev.repository.SubCategoryRepository;
import com.retro.dev.repository.UserCategoryRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;



@Service
public class BusinessService {
    
	@Autowired
	BusinessServicesRepository businessServiceDao;
    
	@Autowired
    UserRepository userRepository;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    UserCategoryRepository userCategoryDao;
    
    
    @Autowired
    SubCategoryRepository subCategoryDao;
    
    @Autowired
    DevUtils utils;
    
    @Autowired
    ChooseOrderRepository chooseOrderRepository;
    
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    
	@Autowired
    ReviewRepository reviewRepository;

    
    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);
    
    public BusinessServicesResponse save(String username, BusinessServicesRequest createBusinessService) {
    	BusinessServicesResponse response = new BusinessServicesResponse();
    	/*if(StringUtils.isEmpty(bService.getServiceId())) {
    		bService.setServiceId(UUID.randomUUID().toString());
    	}*/
    	if(createBusinessService==null) {
    		response.setStatus(PublicConstants.FAIL);
    		response.setRespDesc("mandatory parameters missing");
    		return response;
    	}
    	BusinessServices bServicesDB = new BusinessServices();
    	BeanUtils.copyProperties(createBusinessService, bServicesDB);
    	bServicesDB.setCreatedDate(utils.getCurrentDateAndTime());
    	bServicesDB.setUpdatedDate(bServicesDB.getCreatedDate());
    	businessServiceDao.save(bServicesDB);
    	response.setStatus(PublicConstants.SUCCESS);
		response.setRespDesc("Business Service saved successfully");
        return response;
    }

    public BusinessServicesResponse update(String username, BusinessServicesRequest bService) {
        BusinessServicesResponse response = new BusinessServicesResponse();
		try {
			Optional<BusinessServices> bServiceRead = businessServiceDao.findById(bService.getServiceId());
			BusinessServices bServiceDB = new BusinessServices();
			if(null!=bServiceRead) {
				BeanUtils.copyProperties(bServiceRead, bServiceDB, utils.getNullPropertyNames(bService));
				bServiceDB = saveMediaFilesOnServer(bService, bServiceDB);
			}
			BusinessServices updatedBService = businessServiceDao.save(bServiceDB);
			if (updatedBService != null) {
				response.setStatus(PublicConstants.SUCCESS);
				response.setRespDesc("Business Service saved successfully");
			} else {
				response.setStatus(PublicConstants.FAIL);
				response.setRespDesc("Business Service update failed");
			}
		} catch (BeansException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Business Service update failed: could not get user details");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Business Service update failed: could not save user medias");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Business Service update failed: invalid path in user medias");
			e.printStackTrace();
		} catch (IOException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Business Service update failed: error in saving user medias");
			e.printStackTrace();
		}
        return response;
    }

    private BusinessServices saveMediaFilesOnServer(BusinessServicesRequest bServiceRequest, BusinessServices bServiceDB) throws URISyntaxException, IOException {
    	if(!StringUtils.isEmpty(bServiceDB.getServiceMediaLinks())) {
    		bServiceDB = deletePreviousFiles(bServiceDB);
    	}
    	if(null!=bServiceRequest.getServicesMedias() && bServiceRequest.getServicesMedias().size()>0) {
			String fileUploadDir = utils.getFileUploadDir()+bServiceDB.getUserId()+File.separator;
			for(MultipartFile eachMediaFile: bServiceRequest.getServicesMedias()) {
				if(eachMediaFile.getName().contains(".."))
	                throw new FileNotFoundException("Sorry! one of the File names contains invalid path sequence " + eachMediaFile.getName());
				String originalFileName = eachMediaFile.getOriginalFilename();
				String newFilePath = createResourceSubFolder(fileUploadDir);
				File newServiceMediaImgFile = new File(newFilePath+File.separator+originalFileName);
	            logger.debug("file getting created: "+newServiceMediaImgFile.getPath());
	            eachMediaFile.transferTo(Paths.get(newFilePath+File.separator+originalFileName));
	            if(!StringUtils.isEmpty(bServiceDB.getServiceMediaLinks()))
	            	bServiceDB.setServiceMediaLinks(bServiceDB.getServiceMediaLinks()+","+fileUploadDir+File.separator+originalFileName);
	            else
	            	bServiceDB.setServiceMediaLinks(fileUploadDir+File.separator+originalFileName);
			}
		}
		return bServiceDB;
	}

    private BusinessServices deletePreviousFiles(BusinessServices bServiceDB) {
		String[] prevFileNameArray = bServiceDB.getServiceMediaLinks().split(",");
    	List<String> prevFileNamesList = java.util.Arrays.asList(prevFileNameArray);
		for(String eachFileName : prevFileNamesList) {
			if(!StringUtils.isEmpty(eachFileName)) {
				File eachMediaFile = new File(System.getProperty("user.dir")+File.separator+ eachFileName);
				if(eachMediaFile.exists()) {
					eachMediaFile.delete();
				}
			}
		}
    	return bServiceDB;
	}

private String createResourceSubFolder(String folderName) throws URISyntaxException, IOException {
		
		/*java.net.URL url = JwtUserDetailsService.class.getResource("/media/blank.jpg");
		//java.net.URL url = JwtUserDetailsService.class.getResource(File.separator+"media"+File.separator+PublicConstants.SAMPLE_IMG);
	    if(null!=url) {
		    File fullPathToSubfolder = new File(url.toURI()).getAbsoluteFile();
		    String projectFolder = fullPathToSubfolder.getAbsolutePath().split("target")[0];
		    File testResultsFolder = new File(projectFolder + "src"+File.separator+"main"+File.separator+"resources" + folderName+File.separator);
		    logger.debug("file parent directory: "+testResultsFolder.getPath());
		    if (!testResultsFolder.exists()) {
		    	Files.createDirectories(Paths.get(testResultsFolder+File.separator));
		    }
		    return testResultsFolder.getPath();
	    }else {*/
	    	String currentDirectory = System.getProperty("user.dir")+File.separator+ folderName+File.separator;
	    	logger.debug("file parent directory: "+currentDirectory+File.separator+ folderName+File.separator);
	    	File testResultsFolder = new File(currentDirectory);
	    	if (!testResultsFolder.exists()) {
		    	Files.createDirectories(Paths.get(testResultsFolder+File.separator));
		    }
	    	return currentDirectory;
	    //}
	}
    
	public List<BusinessServicesDTO> listBusinessServices(Principal principal, BusinessServicesRequest listBservices) {
        try {
			List<BusinessServices> bServicesList = businessServiceDao.findAllServices();
			List<UserCategory> userCategoryList = userCategoryDao.findAllByUserId(listBservices.getUserId());
			List<Long> userSubCatList = generateUserSubCategoryList(userCategoryList);
			List<BusinessServicesDTO> bServicesDTOList = new ArrayList<BusinessServicesDTO>();
			for(BusinessServices eachService : bServicesList) {
				int distanceInKM = calculateDistanceInKilometer(eachService.getLatitude(), eachService.getLongitude(),
						listBservices.getLatitude(), listBservices.getLongitude());
				if(distanceInKM!=-999999 && distanceInKM<=10) {
					BusinessServicesDTO eachServiceDTO = new BusinessServicesDTO();
					BeanUtils.copyProperties(eachService, eachServiceDTO);
					if(userSubCatList.contains(eachService.getCategoryId())) {
						eachServiceDTO.setIsUserCategoryFlag("Y");
					}else {
						eachServiceDTO.setIsUserCategoryFlag("N");
					}
					eachServiceDTO = addServiceIconImage(eachServiceDTO);
					bServicesDTOList.add(eachServiceDTO);
				}
			}
			//surroundingBServicesList = surroundingBServicesList.subList(listBservices.getPageNo(), listBservices.getPageNo()*listBservices.getPageSize());
			return bServicesDTOList;
		} catch (Exception e) {
			logger.error("error in listBusinessServices: "+e.getMessage());
			e.printStackTrace();
		}
        return new ArrayList<BusinessServicesDTO>();
    }
    

	private List<Long> generateUserSubCategoryList(List<UserCategory> userCategoryList) {
		List<Long> userSubCatList = new ArrayList<Long>();
		List<SubCategory> userSubCatCompleteList = new ArrayList<SubCategory>();
		for(UserCategory eachUserCat: userCategoryList) {
			List<SubCategory> subCatList = subCategoryDao.findByCategory(eachUserCat.getCategory());
			if (null!=subCatList) {
				userSubCatCompleteList.addAll(subCatList);
			}
    	}
    	if (null!=userSubCatCompleteList) {
    		for(SubCategory eachSubCat : userSubCatCompleteList) {
    			userSubCatList.add(eachSubCat.getSubcategoryId());
    		}
    	}
		return userSubCatList;
	}


	public int calculateDistanceInKilometer(double userLat, double userLng, double venueLat, double venueLng) {
        try {
			double latDistance = Math.toRadians(userLat - venueLat);
			double lngDistance = Math.toRadians(userLng - venueLng);
			double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
						+ Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
							* Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			return (int) (Math.round(PublicConstants.AVERAGE_RADIUS_OF_EARTH_KM * c));
		} catch (Exception e) {
			logger.error("error in calculateDistanceInKilometer: "+e.getMessage());
			e.printStackTrace();
		}
        return -999999;
    }
	

	private BusinessServicesDTO addServiceIconImage(BusinessServicesDTO eachServiceDTO) {
		try {
			if(!StringUtils.isEmpty(eachServiceDTO.getServiceMediaLinks())) {
				String stringEncodedEachMediaFile = null;
				String[] prevFileNameArray = eachServiceDTO.getServiceMediaLinks().split(",");
				if(!StringUtils.isEmpty(prevFileNameArray[0])) {
					File eachMediaFile = new File(prevFileNameArray[0]);
					if(eachMediaFile.exists()) {
						byte[] bgImgfileContent = FileUtils.readFileToByteArray(eachMediaFile);
						stringEncodedEachMediaFile = Base64.getEncoder().encodeToString(bgImgfileContent);
					}
				}
				eachServiceDTO.setServiceIconImg(stringEncodedEachMediaFile);
			}
		} catch (IOException e) {
			logger.error("error in adding ServiceIconImage: "+e.getMessage());
			e.printStackTrace();
		}
		return eachServiceDTO;
	}

    
    public BusinessServicesResponse getBusinessServiceDetail(Principal principal, BusinessServicesRequest listBservices) {
    	Optional<BusinessServices> bserviceDetailDB = businessServiceDao.findById(listBservices.getServiceId());
    	BusinessServicesDTO bServiceDetail;
    	BusinessServicesResponse response = new BusinessServicesResponse();
    	if(bserviceDetailDB==null||!bserviceDetailDB.isPresent()) {
    		return generateFailResponse("Business Service not found.");
    	}
    	bServiceDetail = new BusinessServicesDTO();
		BeanUtils.copyProperties(bserviceDetailDB.get(), bServiceDetail);
		response = setBusinessServiceUserDetails(bServiceDetail, response);
		if(null!=response.getStatus()&&response.getStatus().equalsIgnoreCase(PublicConstants.FAIL)) {
			return generateFailResponse("Business Service User not found.");
		}
		response = setCategoryDetails(bServiceDetail, response);
		if(null!=response.getStatus()&&response.getStatus().equalsIgnoreCase(PublicConstants.FAIL)) {
			return generateFailResponse("Business Service Category not found.");
		}
		response = fetchServiceMediaFiles(bServiceDetail, response);
    	return generateSuccessResponse("Business Service retrieved", bServiceDetail, response);
    }
    
    private BusinessServicesResponse setCategoryDetails(BusinessServicesDTO bServiceDetail,
			BusinessServicesResponse response) {
    	
    	SubCategory subCat = subCategoryDao.findBySubcategoryId(bServiceDetail.getCategoryId());
    	if (null!=subCat) {
    		bServiceDetail.setSubcategory(subCat.getSubcategory());
    		bServiceDetail.setCategory(subCat.getCategory().getCategory());
    	} else {
	    	Category serviceCategoryDetails = categoryService.findCategoryById(bServiceDetail.getCategoryId());
	    	if(null==serviceCategoryDetails) {
	    		return generateFailResponse("Business Service Category not found.");
	    	}
	    	bServiceDetail.setCategory(serviceCategoryDetails.getCategory());
    	}
		return response;
	}

	private BusinessServicesResponse setBusinessServiceUserDetails(BusinessServicesDTO bServiceDetail,
			BusinessServicesResponse response) {
    	Optional<User> bServiceUser = userRepository.findById(bServiceDetail.getUserId());
    	if(bServiceUser==null||!bServiceUser.isPresent()) {
			return generateFailResponse("Business Service User not found.");
		}
    	bServiceDetail.setUserName(bServiceUser.get().getUsername());
    	bServiceDetail.setName(bServiceUser.get().getName());
    	if(!StringUtils.isEmpty(bServiceUser.get().getProfileImage())) {
			File userProfileImg = new File(System.getProperty("user.dir")+File.separator+ bServiceUser.get().getProfileImage());
			String stringEncodedProfileImg = "";
			if(userProfileImg.exists()) {
				try {
					byte[] bgImgfileContent = FileUtils.readFileToByteArray(userProfileImg);
					stringEncodedProfileImg = Base64.getEncoder().encodeToString(bgImgfileContent);
				} catch (IOException e) {
					logger.error("error generating User Profile in setBusinessServiceUserDetails");
					e.printStackTrace();
				}
			}
			bServiceDetail.setProfileImage(stringEncodedProfileImg);
		}
		return response;
	}

	private BusinessServicesResponse fetchServiceMediaFiles(BusinessServicesDTO bServiceDetail, BusinessServicesResponse response) {
		try {
			if(!StringUtils.isEmpty(bServiceDetail.getServiceMediaLinks())) {
				String[] prevFileNameArray = bServiceDetail.getServiceMediaLinks().split(",");
				List<String> prevFileNamesList = java.util.Arrays.asList(prevFileNameArray);
				List<String> base64mediaFiles = new ArrayList<String>();
				for(String eachFileName : prevFileNamesList) {
					if(!StringUtils.isEmpty(eachFileName)) {
						File eachMediaFile = new File(System.getProperty("user.dir")+File.separator+ eachFileName);
						if(eachMediaFile.exists()) {
							byte[] bgImgfileContent = FileUtils.readFileToByteArray(eachMediaFile);
							String stringEncodedEachMediaFile = Base64.getEncoder().encodeToString(bgImgfileContent);
							base64mediaFiles.add(stringEncodedEachMediaFile);
						}
					}
				}
				response.setBase64mediaFiles(base64mediaFiles);
			}
		} catch (IOException e) {
			logger.error("error in fetching ServiceMediaFiles: "+e.getMessage());
			e.printStackTrace();
		}
    	return response;
	}
	
	
	public BusinessServicesResponse generateFailResponse(String msg) {
		BusinessServicesResponse response = new BusinessServicesResponse();
		BusinessServicesDTO bServiceDetail = new BusinessServicesDTO();
		response.setStatus(PublicConstants.FAIL);
		response.setRespDesc(msg);
		response.setBusinessServices(bServiceDetail);
		return response;
	}
	
	public BusinessServicesResponse generateSuccessResponse(String msg, BusinessServicesDTO bServiceDetail, BusinessServicesResponse response) {
		response.setStatus(PublicConstants.SUCCESS);
		response.setRespDesc(msg);
		response.setBusinessServices(bServiceDetail);
		return response;
	}

}
