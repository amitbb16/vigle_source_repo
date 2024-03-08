package com.retro.dev.security.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.FavouritesDTO;
import com.retro.dev.models.BusinessServices;
import com.retro.dev.models.Favourites;
import com.retro.dev.payload.request.FavouritesRequest;
import com.retro.dev.payload.response.FavouritesResponse;
import com.retro.dev.repository.BusinessServicesRepository;
import com.retro.dev.repository.FavouritesRepository;
import com.retro.dev.util.PublicConstants;



@Service
public class FavouriteServices {
    
	@Autowired
	FavouritesRepository favouritesDao;
    
	@Autowired
	BusinessServicesRepository businessServiceDao;
    
    
    private static final Logger logger = LoggerFactory.getLogger(FavouriteServices.class);
    
    public FavouritesResponse save(String username, FavouritesRequest favouritesRequest) {
    	FavouritesResponse response = new FavouritesResponse();
    	Favourites favourites = new Favourites();
    	try {
			Favourites favsFromDB = findExistingFavourite(favouritesRequest);
    		if(null!=favsFromDB) {
    			BeanUtils.copyProperties(favouritesRequest.getFavourites(), favsFromDB);
    			int retrieveUserIdFromServiceStatus = findLikedOrCommentedOnUser(favourites);
    			if(retrieveUserIdFromServiceStatus==1) {
    				favourites = favouritesDao.save(favourites);
    			} else {
    				return generateFailResponse("service or service user no more available.", null);
    			}
    		} else {
    			BeanUtils.copyProperties(favouritesRequest.getFavourites(), favourites);
    			int retrieveUserIdFromServiceStatus = findLikedOrCommentedOnUser(favourites);
    			if(retrieveUserIdFromServiceStatus==1) {
    				favourites = favouritesDao.save(favourites);
    			} else {
    				return generateFailResponse("service or service user no more available.", null);
    			}
    		}
		} catch (BeansException e) {
			logger.error("failed to save favourite");
			e.printStackTrace();
		}
    	BeanUtils.copyProperties(favourites, favouritesRequest.getFavourites());
    	response.setFavourites(favouritesRequest.getFavourites());
    	return generateSuccessResponse("Favourites saved successfully", response);
    }

	private Favourites findExistingFavourite(FavouritesRequest favouritesRequest) {
		Favourites favsFromDB = new Favourites();
		if(null!=favouritesRequest.getFavourites().getServiceId()) {
			favsFromDB = favouritesDao.findByUserIdAndServiceId(favouritesRequest.getFavourites().getUserId(), 
					favouritesRequest.getFavourites().getServiceId());
		} else if(null!=favouritesRequest.getFavourites().getOrderId()) {
			favsFromDB = favouritesDao.findByUserIdAndOrderId(favouritesRequest.getFavourites().getUserId(), 
					favouritesRequest.getFavourites().getOrderId());
		} else if(null!=favouritesRequest.getFavourites().getFeedId()) {
			favsFromDB = favouritesDao.findByUserIdAndFeedId(favouritesRequest.getFavourites().getUserId(), 
					favouritesRequest.getFavourites().getFeedId());
		}
		return favsFromDB;
	}

	
    /****************************************************
     * this method needs to be corrected in future w.r.t.
     * the search Target UserId based on OrderId
     ****************************************************/
    private int findLikedOrCommentedOnUser(Favourites favourites) {
    	Optional<BusinessServices> services = null;
    	if(null!=favourites.getServiceId()) {
    		services = businessServiceDao.findById(favourites.getServiceId());
		} else if(null!=favourites.getOrderId()) {
			services = businessServiceDao.findById(favourites.getOrderId());
		} else if(null!=favourites.getFeedId()) {
			services = businessServiceDao.findById(favourites.getFeedId());
		}
		if(null!=services && services.isPresent()) {
			favourites.setFavouriteUserId(services.get().getUserId());
			return 1;
		} else {
			return 0;
		}
	}

	public FavouritesResponse removeFromFavourite(String username, FavouritesRequest favouritesRequest) {
    	FavouritesResponse response = new FavouritesResponse();
		Favourites favourites = favouritesDao.findById(favouritesRequest.getFavourites().getId());
		if(null==favourites) {
			return generateFailResponse("Favourites not found", null);
		}
		favouritesDao.delete(favourites);
		response.setFavourites(favouritesRequest.getFavourites());
    	return generateSuccessResponse("Favourites removed successfully", response);
    }

    
	public FavouritesResponse listAllFavouritesData(Principal principal, FavouritesRequest listBservices) {
		FavouritesResponse response = new FavouritesResponse();
		try {
			List<Favourites> allFavouritesDataList = favouritesDao.findAll();
			if(null!=allFavouritesDataList && allFavouritesDataList.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<FavouritesDTO> allFavouritesList = new ArrayList<FavouritesDTO>();
			for(Favourites eachFavourite : allFavouritesDataList) {
				FavouritesDTO eachFavDTO = new FavouritesDTO();
				BeanUtils.copyProperties(eachFavourite, eachFavDTO);
				allFavouritesList.add(eachFavDTO);
			}
			response.setFavouritesList(allFavouritesList);
		} catch (Exception e) {
			logger.error("error in listAllFavouritesData: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Favourites returned successfully", response);
        return response;
    }
    

	public FavouritesResponse listAllUserFavourites(Principal principal, FavouritesRequest listFavourites) {
		FavouritesResponse response = new FavouritesResponse();
		try {
			List<Favourites> userFavsList = favouritesDao.findAllByUserId(listFavourites.getFavourites().getUserId());
			if(null!=userFavsList && userFavsList.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<FavouritesDTO> allUserFavouritesDTOList = new ArrayList<FavouritesDTO>();
			for(Favourites eachFavourite : userFavsList) {
				FavouritesDTO eachFavDTO = new FavouritesDTO();
				BeanUtils.copyProperties(eachFavourite, eachFavDTO);
				allUserFavouritesDTOList.add(eachFavDTO);
			}
			response.setFavouritesList(allUserFavouritesDTOList);
		} catch (Exception e) {
			logger.error("error in listAllUserFavourites: "+e.getMessage());
			e.printStackTrace();
		}
		return generateSuccessResponse("List of All User Favourites returned successfully", response);
	}

	public FavouritesResponse listAllFavouritesBasedOnService(Principal principal, FavouritesRequest listFavourites) {
		FavouritesResponse response = new FavouritesResponse();
		try {
			List<Favourites> favsListBasedOnService = favouritesDao.findAllByServiceId(listFavourites.getFavourites().getServiceId());
			if(null!=favsListBasedOnService && favsListBasedOnService.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<FavouritesDTO> allUserFavouritesDTOList = new ArrayList<FavouritesDTO>();
			for(Favourites eachFavourite : favsListBasedOnService) {
				FavouritesDTO eachFavDTO = new FavouritesDTO();
				BeanUtils.copyProperties(eachFavourite, eachFavDTO);
				allUserFavouritesDTOList.add(eachFavDTO);
			}
			response.setFavouritesList(allUserFavouritesDTOList);
		} catch (Exception e) {
			logger.error("error in listAllFavouritesBasedOnService: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Favourites Based On Service returned successfully", response);
        return response;
	}

	public FavouritesResponse listAllFavouritesBasedOnFeed(Principal principal, FavouritesRequest listFavourites) {
		FavouritesResponse response = new FavouritesResponse();
		try {
			List<Favourites> favsListBasedOnFeed = favouritesDao.findAllByFeedId(listFavourites.getFavourites().getFeedId());
			if(null!=favsListBasedOnFeed && favsListBasedOnFeed.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<FavouritesDTO> allUserFavouritesDTOList = new ArrayList<FavouritesDTO>();
			for(Favourites eachFavourite : favsListBasedOnFeed) {
				FavouritesDTO eachFavDTO = new FavouritesDTO();
				BeanUtils.copyProperties(eachFavourite, eachFavDTO);
				allUserFavouritesDTOList.add(eachFavDTO);
			}
			response.setFavouritesList(allUserFavouritesDTOList);
		} catch (Exception e) {
			logger.error("error in listAllFavouritesBasedOnFeed: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Favourites Based On Service returned successfully", response);
        return response;
	}
    
    public FavouritesResponse getFavouritesDetail(Principal principal, FavouritesRequest listBservices) {
    	FavouritesResponse response = new FavouritesResponse();
    	Favourites eachFavourite = favouritesDao.findById(listBservices.getFavourites().getId());
    	FavouritesDTO eachFavDTO = new FavouritesDTO();
		BeanUtils.copyProperties(eachFavourite, eachFavDTO);
		response.setFavourites(eachFavDTO);
    	return generateSuccessResponse("Favourites Details retrieved successfully", response);
    }

	public FavouritesResponse getServiceSpecificUserFavouriteDetail(String name, FavouritesRequest favouritesRequest) {
		FavouritesResponse response = new FavouritesResponse();
    	FavouritesDTO favourites = new FavouritesDTO();
    	Favourites favsFromDB = new Favourites();
    	try {
    		if(null!=favouritesRequest.getFavourites().getServiceId()) {
				favsFromDB = favouritesDao.findByUserIdAndServiceId(favouritesRequest.getFavourites().getUserId(), 
						favouritesRequest.getFavourites().getServiceId());
			}
    		if(null==favsFromDB) {
    			return generateFailResponse("this favourite not found!", null);
    		}
    		BeanUtils.copyProperties(favsFromDB, favourites);
		} catch (BeansException e) {
			logger.error("failed to get Service Specific User Favourite Detail");
			e.printStackTrace();
		}
    	response.setFavourites(favourites);
    	return generateSuccessResponse("Service Specific User Favourite Detail fetched successfully", response);
	}

	public FavouritesResponse getOrderSpecificUserFavouritesDetail(String name, FavouritesRequest favouritesRequest) {
		FavouritesResponse response = new FavouritesResponse();
    	FavouritesDTO favourites = new FavouritesDTO();
    	Favourites favsFromDB = new Favourites();
    	try {
    		if(null!=favouritesRequest.getFavourites().getOrderId()) {
				favsFromDB = favouritesDao.findByUserIdAndOrderId(favouritesRequest.getFavourites().getUserId(), 
						favouritesRequest.getFavourites().getOrderId());
			}
    		if(null==favsFromDB) {
    			return generateFailResponse("this favourite not found!",null);
    		}
    		BeanUtils.copyProperties(favsFromDB, favourites);
		} catch (BeansException e) {
			logger.error("failed to get Order Specific User Favourite Detail");
			e.printStackTrace();
		}
    	response.setFavourites(favourites);
    	return generateSuccessResponse("Order Specific User Favourite Detail fetched successfully", response);
	}

	
	public FavouritesResponse generateFailResponse(String msg, FavouritesDTO favDTO) {
		FavouritesResponse response = new FavouritesResponse();
		favDTO = null!=favDTO? favDTO:new FavouritesDTO();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		response.setFavourites(favDTO);
		return response;
	}
	
	public FavouritesResponse generateSuccessResponse(String msg, FavouritesResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}


}
