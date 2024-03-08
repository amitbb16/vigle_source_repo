package com.retro.dev.security.services;

import com.retro.dev.dtos.BusinessServicesDTO;
import com.retro.dev.models.BusinessServices;
import com.retro.dev.models.Category;
import com.retro.dev.models.ServiceRequest;
import com.retro.dev.models.SubCategory;
import com.retro.dev.models.User;
import com.retro.dev.models.UserCategory;
import com.retro.dev.payload.request.BusinessServicesRequest;
import com.retro.dev.payload.request.ServiceRequestRequest;
import com.retro.dev.payload.response.BusinessServicesResponse;
import com.retro.dev.payload.response.MessageResponse;
import com.retro.dev.repository.BusinessServicesRepository;
import com.retro.dev.repository.ServiceRequestRepository;
import com.retro.dev.repository.SubCategoryRepository;
import com.retro.dev.repository.UserCategoryRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceRequestService {
    
	@Autowired
    ServiceRequestRepository serviceRequestRepository;
    
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
	BusinessServicesRepository userOrdersDao;


    
    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);
    
    public ResponseEntity<?> save(String username, ServiceRequestRequest serviceRequest) {
    	try {
	    	Optional<User> user = userRepository.findByUsername(username);
	        ServiceRequest newServiceRequest = new ServiceRequest(user.get().getId(),
	                serviceRequest.getServiceReq().getName(), serviceRequest.getServiceReq().getDescription(),
	                serviceRequest.getServiceReq().getPrice(), serviceRequest.getServiceReq().getCompletiondate(),
	                serviceRequest.getServiceReq().getMessage(), serviceRequest.getServiceReq().getStatus(), 
	                serviceRequest.getServiceReq().getServiceId());
	        updateMediaFiles(serviceRequest);
	        newServiceRequest.setMediaPaths(serviceRequest.getServiceReq().getMediaPaths());
	        newServiceRequest.setCreatedDate(serviceRequest.getServiceReq().getCreatedDate());
	        newServiceRequest.setLastUpdatedDate(serviceRequest.getServiceReq().getLastUpdatedDate());
	        ServiceRequest dealres = serviceRequestRepository.save(newServiceRequest);
	        if (dealres != null)
	            return ResponseEntity.ok(new MessageResponse("serviceRequest saved"));
	        else
	            return ResponseEntity.ok(new MessageResponse("failed to save the serviceRequest"));
    	} catch (IOException e) {
			logger.error("error in ServiceRequest creation");
			e.printStackTrace();
		} 
    	return ResponseEntity.ok(new MessageResponse("failed to save the serviceRequest"));
    }

    public ResponseEntity<?> update(String username, ServiceRequestRequest serviceRequest) {
        try {
			ServiceRequest serviceRequestDB = serviceRequestRepository.findById(serviceRequest.getServiceReq().getId());
			serviceRequestDB.setName(serviceRequest.getServiceReq().getName());
			serviceRequestDB.setDescription(serviceRequest.getServiceReq().getDescription());
			serviceRequestDB.setPrice(serviceRequest.getServiceReq().getPrice());
			serviceRequestDB.setCompletiondate(serviceRequest.getServiceReq().getCompletiondate());
			serviceRequestDB.setMessage(serviceRequest.getServiceReq().getMessage());
			updateMediaFiles(serviceRequest);
			ServiceRequest dealres = serviceRequestRepository.save(serviceRequestDB);
			if (dealres != null)
			    return ResponseEntity.ok("serviceRequest updated");
			else
			    return ResponseEntity.badRequest().body("failed to update the serviceRequest");
		} catch (IOException e) {
			logger.error("error in ServiceRequest update");
			e.printStackTrace();
		}
        return ResponseEntity.badRequest().body("failed to update the serviceRequest");
    }


    private void updateMediaFiles(ServiceRequestRequest serviceRequest) throws IOException {
		if(null!=serviceRequest.getServiceReq().getServicesRequestMedias() && serviceRequest.getServiceReq().getServicesRequestMedias().size()>0) {
			String fileUploadDir = utils.getFileUploadDir()+serviceRequest.getServiceReq().getUserid()+File.separator;
			for(MultipartFile eachMediaFile: serviceRequest.getServiceReq().getServicesRequestMedias()) {
				if(eachMediaFile.getName().contains(".."))
	                throw new FileNotFoundException("Sorry! one of the File names contains invalid path sequence " + eachMediaFile.getName());
				String originalFileName = eachMediaFile.getOriginalFilename();
				String newFilePath = System.getProperty("user.dir")+File.separator+ fileUploadDir+File.separator;
		    	logger.debug("file parent directory: "+newFilePath+File.separator+ fileUploadDir+File.separator);
		    	File testResultsFolder = new File(newFilePath);
		    	if (!testResultsFolder.exists()) {
			    	Files.createDirectories(Paths.get(testResultsFolder+File.separator));
			    }
				File newServiceMediaImgFile = new File(newFilePath+File.separator+originalFileName);
	            logger.debug("file getting created: "+newServiceMediaImgFile.getPath());
	            eachMediaFile.transferTo(Paths.get(newFilePath+File.separator+originalFileName));
	            if(!StringUtils.isEmpty(serviceRequest.getServiceReq().getMediaPaths()))
	            	serviceRequest.getServiceReq().setMediaPaths(serviceRequest.getServiceReq().getMediaPaths()+","+fileUploadDir+File.separator+originalFileName);
	            else
	            	serviceRequest.getServiceReq().setMediaPaths(fileUploadDir+File.separator+originalFileName);
			}
		}
		
	}

	public List<ServiceRequest> listServiceRequests(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return serviceRequestRepository.findAllByUserid(user.get().getId());

    }

    
    
    public BusinessServicesResponse createOrder(String username, BusinessServicesRequest createBusinessService) {
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
    	userOrdersDao.save(bServicesDB);
    	response.setStatus(PublicConstants.SUCCESS);
		response.setRespDesc("Order saved successfully");
        return response;
    }

    public BusinessServicesResponse getUserOrderDetail(Principal principal, BusinessServicesRequest listBservices) {
    	Optional<BusinessServices> bserviceDetailDB = userOrdersDao.findById(listBservices.getOrderId());
    	BusinessServicesDTO bServiceDetail;
    	BusinessServicesResponse response = new BusinessServicesResponse();
    	if(bserviceDetailDB==null||!bserviceDetailDB.isPresent()) {
    		return generateFailResponse("Order not found.");
    	}
    	bServiceDetail = new BusinessServicesDTO();
		BeanUtils.copyProperties(bserviceDetailDB.get(), bServiceDetail);
		bServiceDetail.setOrderId(listBservices.getOrderId());
		response = setOrderUserDetails(bServiceDetail, response);
		if(null!=response.getStatus()&&response.getStatus().equalsIgnoreCase(PublicConstants.FAIL)) {
			return generateFailResponse("Order User not found.");
		}
		response = setCategoryDetails(bServiceDetail, response);
		if(null!=response.getStatus()&&response.getStatus().equalsIgnoreCase(PublicConstants.FAIL)) {
			return generateFailResponse("Order Category not found.");
		}
		response = fetchOrderMediaFiles(bServiceDetail, response);
    	return generateSuccessResponse("Order retrieved", bServiceDetail, response);
    }
    
	private BusinessServicesResponse setOrderUserDetails(BusinessServicesDTO bServiceDetail,
			BusinessServicesResponse response) {
    	Optional<User> bServiceUser = userRepository.findById(bServiceDetail.getUserId());
    	if(bServiceUser==null||!bServiceUser.isPresent()) {
			return generateFailResponse("Order User not found.");
		}
    	bServiceDetail.setUserName(bServiceUser.get().getUsername());
    	bServiceDetail.setName(bServiceUser.get().getName());
    	if(!StringUtils.isEmpty(bServiceUser.get().getProfileImage())) {
			File userProfileImg = new File(bServiceUser.get().getProfileImage());
			String stringEncodedProfileImg = "";
			if(userProfileImg.exists()) {
				try {
					byte[] bgImgfileContent = FileUtils.readFileToByteArray(userProfileImg);
					stringEncodedProfileImg = Base64.getEncoder().encodeToString(bgImgfileContent);
				} catch (IOException e) {
					logger.error("error generating User Profile in setOrderUserDetails");
					e.printStackTrace();
				}
			}
			bServiceDetail.setProfileImage(stringEncodedProfileImg);
		}
		return response;
	}
    
    private BusinessServicesResponse setCategoryDetails(BusinessServicesDTO orderDetail,
			BusinessServicesResponse response) {
    	
    	SubCategory subCat = subCategoryDao.findBySubcategoryId(orderDetail.getCategoryId());
    	if (null!=subCat) {
    		orderDetail.setSubcategory(subCat.getSubcategory());
    		orderDetail.setCategory(subCat.getCategory().getCategory());
    	} else {
	    	Category serviceCategoryDetails = categoryService.findCategoryById(orderDetail.getCategoryId());
	    	if(null==serviceCategoryDetails) {
	    		return generateFailResponse("Order Category not found.");
	    	}
	    	orderDetail.setCategory(serviceCategoryDetails.getCategory());
    	}
		return response;
	}
    
    private BusinessServicesResponse fetchOrderMediaFiles(BusinessServicesDTO bServiceDetail, BusinessServicesResponse response) {
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
			logger.error("error in fetching OrderMediaFiles: "+e.getMessage());
			e.printStackTrace();
		}
    	return response;
	}
	
    
    public List<BusinessServicesDTO> getAllOrdersList(Principal principal, BusinessServicesRequest listBservices) {
        try {
			List<BusinessServices> bServicesList = userOrdersDao.findAllOrders();
			List<BusinessServices> surroundingBServicesList = new ArrayList<BusinessServices>();
			List<UserCategory> userCategoryList = userCategoryDao.findAllByUserId(listBservices.getUserId());
			List<Long> userSubCatList = generateUserSubCategoryList(userCategoryList);
			List<BusinessServicesDTO> bServicesDTOList = new ArrayList<BusinessServicesDTO>();
			for(BusinessServices eachService : bServicesList) {
				int distanceInKM = calculateDistanceInKilometer(eachService.getLatitude(), eachService.getLongitude(),
						listBservices.getLatitude(), listBservices.getLongitude());
				if(distanceInKM!=-999999 && distanceInKM<=10) {
					surroundingBServicesList.add(eachService);
					BusinessServicesDTO eachServiceDTO = new BusinessServicesDTO();
					BeanUtils.copyProperties(eachService, eachServiceDTO);
					if(userSubCatList.contains(eachService.getCategoryId())) {
						eachServiceDTO.setIsUserCategoryFlag("Y");
					}else {
						eachServiceDTO.setIsUserCategoryFlag("N");
					}
					eachServiceDTO = addOrderIconImage(eachServiceDTO);
					eachServiceDTO.setOrderId(eachServiceDTO.getServiceId());
					bServicesDTOList.add(eachServiceDTO);
				}
			}
			//surroundingBServicesList = surroundingBServicesList.subList(listBservices.getPageNo(), listBservices.getPageNo()*listBservices.getPageSize());
			return bServicesDTOList;
		} catch (Exception e) {
			logger.error("error in getAllOrdersList: "+e.getMessage());
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
	
	private BusinessServicesDTO addOrderIconImage(BusinessServicesDTO eachServiceDTO) {
		try {
			if(!StringUtils.isEmpty(eachServiceDTO.getServiceMediaLinks())) {
				String stringEncodedEachMediaFile = null;
				String[] prevFileNameArray = eachServiceDTO.getServiceMediaLinks().split(",");
				if(!StringUtils.isEmpty(prevFileNameArray[0])) {
					File eachMediaFile = new File(System.getProperty("user.dir")+File.separator+ prevFileNameArray[0]);
					if(eachMediaFile.exists()) {
						byte[] bgImgfileContent = FileUtils.readFileToByteArray(eachMediaFile);
						stringEncodedEachMediaFile = Base64.getEncoder().encodeToString(bgImgfileContent);
					}
				}
				eachServiceDTO.setServiceIconImg(stringEncodedEachMediaFile);
			}
		} catch (IOException e) {
			logger.error("error in adding OrderIconImage: "+e.getMessage());
			e.printStackTrace();
		}
		return eachServiceDTO;
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
