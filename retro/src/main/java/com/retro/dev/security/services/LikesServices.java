package com.retro.dev.security.services;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.LikesDTO;
import com.retro.dev.models.BusinessServices;
import com.retro.dev.models.Likes;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.LikesRequest;
import com.retro.dev.payload.response.LikesResponse;
import com.retro.dev.repository.BusinessServicesRepository;
import com.retro.dev.repository.LikesRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.PublicConstants;



@Service
public class LikesServices {
    
	@Autowired
	LikesRepository likesDao;
    
	@Autowired
	BusinessServicesRepository businessServiceDao;
    
	@Autowired
    UserRepository userRepository;
    
    
    private static final Logger logger = LoggerFactory.getLogger(LikesServices.class);



    public LikesResponse addLike(String name, LikesRequest LikesRequest) {
    	LikesResponse response = new LikesResponse();
    	Likes likes = new Likes();
    	try {
    		response = validateLikesRequest(LikesRequest);
    		if(StringUtils.isNotEmpty(response.getResponseStatus())&&
    				response.getResponseStatus().equalsIgnoreCase(PublicConstants.FAIL)) {
    			return response;
    		}
    		BeanUtils.copyProperties(LikesRequest.getLikesDTO(), likes);
    		Long targetUserId = likes.getLikedToUserId();
    		if(targetUserId!=null) {
    			/*if (LikesRequest.getLikesDTO().getIsCommented().equalsIgnoreCase("Y")) {
    				likes.setCommentedOnUserId(targetUserId);
    			}*/
    			if (LikesRequest.getLikesDTO().getIsLiked().equalsIgnoreCase("Y")) {
    				likes.setLikedToUserId(targetUserId);
    			}
    			likes = likesDao.save(likes);
    		} else {
    			return generateFailResponse("user no more available.", null, null);
    		}
    	} catch (BeansException e) {
    		logger.error("failed to save Like");
    		e.printStackTrace();
    	}
    	BeanUtils.copyProperties(likes, LikesRequest.getLikesDTO());
    	response.setLikesDTO(LikesRequest.getLikesDTO());
    	return generateSuccessResponse("Likes saved successfully", response);
    }


    private LikesResponse validateLikesRequest(LikesRequest LikesRequest) {
    	LikesResponse response = new LikesResponse();
    	if(LikesRequest.getLikesDTO().getServiceId() ==null && LikesRequest.getLikesDTO().getOrderId() ==null 
    			&& LikesRequest.getLikesDTO().getFeedsId() ==null) {
    		return generateFailResponse("Please provide either serviceId or orderId or feedsId.", null, null);
    	}
    	if(StringUtils.isNotBlank(LikesRequest.getLikesDTO().getIsCommented()) && 
    			LikesRequest.getLikesDTO().getIsCommented().equalsIgnoreCase("Y")) {
    		if(StringUtils.isBlank(LikesRequest.getLikesDTO().getComment())) {
    			return generateFailResponse("Please provide a proper comment.", null, null);
    		}
    		if(StringUtils.isBlank(LikesRequest.getLikesDTO().getIsReplyComment())) {
    			return generateFailResponse("Please provide a proper Y/N value for isReplyComment field.", null, null);
    		}
    		
    	}
		//below condition not required anymore, handled at targetUserId setting
    	/*if(StringUtils.isNotBlank(LikesRequest.getLikesDTO().getIsLiked()) && 
    			LikesRequest.getLikesDTO().getIsLiked().equalsIgnoreCase("Y")) {
    		if(LikesRequest.getLikesDTO().getLikedToUserId()==null) {
    			return generateFailResponse("Please provide a value for likedToUserId.", null, null);
    		}
    	}*/
		return response;
	}


	/****************************************************
     * this method needs to be corrected in future w.r.t.
     * the search Target UserId based on OrderId
     ****************************************************/
    @SuppressWarnings("unused")
	private Long retrieveTargetUserId(Likes likes) {
    	Long userId = null;
    	if(null!=likes.getLikedToServiceId()) {
    		Optional<BusinessServices> serviceObj = businessServiceDao.findById(likes.getLikedToServiceId());
    		userId = null!=serviceObj? serviceObj.get().getUserId():null;
    	} else if(null!=likes.getLikedToOrderId()) {
    		Optional<BusinessServices> serviceObj = businessServiceDao.findById(likes.getLikedToOrderId());
    		userId = null!=serviceObj? serviceObj.get().getUserId():null;
    	} else if(null!=likes.getLikedToFeedsId()) {
    		Likes targetUserIdComment = likesDao.findById(likes.getLikedToFeedsId());
    		userId = null!=targetUserIdComment? targetUserIdComment.getLikedByUserId():0;
    	}
    	return userId;
    }


	public LikesResponse updateLike(String name, LikesRequest LikesRequest) {
		if(null==LikesRequest.getLikesDTO().getId()) {
			return generateFailResponse("Please select/provide the Like id to update.", null, null);
		}
		Likes storedComment = likesDao.findById(LikesRequest.getLikesDTO().getId());
		LocalDateTime createdDate = storedComment.getCreatedDate();
		BeanUtils.copyProperties(LikesRequest.getLikesDTO(), storedComment);	//adding new request values to storedComment
		BeanUtils.copyProperties(storedComment, LikesRequest.getLikesDTO());	//copying merged values to send to service
		LikesRequest.getLikesDTO().setCreatedDate(createdDate);
		return addLike(name, LikesRequest);
	}
	

	
	public LikesResponse removeLike(String name, LikesRequest LikesRequest) {
		LikesResponse response = new LikesResponse();
		Likes comment = likesDao.findById(LikesRequest.getLikesDTO().getId());
		if(null==comment) {
			return generateFailResponse("Like not found", null, null);
		}
		likesDao.delete(comment);
		response.setLikesDTO(LikesRequest.getLikesDTO());
    	return generateSuccessResponse("Like removed successfully", response);
	}
	
	
	public LikesResponse listAllLikes(Principal principal, LikesRequest listLikes) {
		LikesResponse response = new LikesResponse();
		try {
			List<Likes> allLikesDataList = likesDao.findAll();
			if(null!=allLikesDataList && allLikesDataList.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<LikesDTO> allLikesList = new ArrayList<LikesDTO>();
			for(Likes eachLike : allLikesDataList) {
				LikesDTO eachCommentDTO = new LikesDTO();
				BeanUtils.copyProperties(eachLike, eachCommentDTO);
				allLikesList.add(eachCommentDTO);
			}
			response.setLikesList(allLikesList);
		} catch (Exception e) {
			logger.error("error in listAllLikes: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Likes returned successfully", response);
        return response;
	}


	public LikesResponse listAllLikesBasedOnUser(Principal principal, LikesRequest LikesRequest) {
		LikesResponse response = new LikesResponse();
		try {
			List<Likes> userLikesList = likesDao.findAllByLikedByUserId(LikesRequest.getLikesDTO().getUserId());
			if(null!=userLikesList && userLikesList.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<LikesDTO> allUserLikesDTOList = new ArrayList<LikesDTO>();
			for(Likes eachComment : userLikesList) {
				LikesDTO eachLikeDTO = new LikesDTO();
				BeanUtils.copyProperties(eachComment, eachLikeDTO);
				allUserLikesDTOList.add(eachLikeDTO);
			}
			response.setLikesList(allUserLikesDTOList);
		} catch (Exception e) {
			logger.error("error in listAllLikesBasedOnUser: "+e.getMessage());
			e.printStackTrace();
		}
		return generateSuccessResponse("List of All User Likes returned successfully", response);
	}


	public LikesResponse listAllLikesBasedOnService(Principal principal, LikesRequest LikesRequest) {
		LikesResponse response = new LikesResponse();
		try {
			List<Likes> LikesListBasedOnService = likesDao.findAllByLikedToServiceId(LikesRequest.getLikesDTO().getServiceId());
			if(null!=LikesListBasedOnService && LikesListBasedOnService.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<LikesDTO> allUserLikesDTOList = new ArrayList<LikesDTO>();
			for(Likes eachLike : LikesListBasedOnService) {
				LikesDTO eachLikeDTO = new LikesDTO();
				BeanUtils.copyProperties(eachLike, eachLikeDTO);
				allUserLikesDTOList.add(eachLikeDTO);
			}
			response.setLikesList(allUserLikesDTOList);
		} catch (Exception e) {
			logger.error("error in listAllLikesBasedOnService: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Likes Based On Service returned successfully", response);
        return response;
	}


	public LikesResponse listAllLikesBasedOnCommentedOnUserId(Principal principal, LikesRequest LikesRequest) {
		LikesResponse response = new LikesResponse();
		try {
			Optional<User> userObj = userRepository.findByUsername(principal.getName());
	    	if(userObj==null||!userObj.isPresent()) {
				return generateFailResponse("UserName not found.", null, null);
			}
			List<Likes> likesListBasedOnLikedToUserId = likesDao.findByLikedByUserIdAndCommentedOnUserId(userObj.get().getId(), 
					LikesRequest.getLikesDTO().getCommentedOnUserId());
			if(null!=likesListBasedOnLikedToUserId && likesListBasedOnLikedToUserId.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<LikesDTO> allUserLikesDTOList = new ArrayList<LikesDTO>();
			for(Likes eachLike : likesListBasedOnLikedToUserId) {
				LikesDTO eachLikeDTO = new LikesDTO();
				BeanUtils.copyProperties(eachLike, eachLikeDTO);
				allUserLikesDTOList.add(eachLikeDTO);
			}
			response.setLikesList(allUserLikesDTOList);
		} catch (Exception e) {
			logger.error("error in listAllLikesBasedOnCommentedOnUserId: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Likes Based On CommentedOnUserId returned successfully", response);
        return response;
	}


	public LikesResponse listAllLikesBasedOnlikedToUserId(Principal principal, LikesRequest LikesRequest) {
		LikesResponse response = new LikesResponse();
		try {
			Optional<User> userObj = userRepository.findByUsername(principal.getName());
	    	if(userObj==null||!userObj.isPresent()) {
				return generateFailResponse("UserName not found.", null, null);
			}
			List<Likes> LikesListBasedOnLikedToUserId = likesDao.findByLikedByUserIdAndLikedToUserId(userObj.get().getId(), 
					LikesRequest.getLikesDTO().getLikedToUserId());
			if(null!=LikesListBasedOnLikedToUserId && LikesListBasedOnLikedToUserId.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<LikesDTO> allUserLikesDTOList = new ArrayList<LikesDTO>();
			for(Likes eachLike : LikesListBasedOnLikedToUserId) {
				LikesDTO eachLikeDTO = new LikesDTO();
				BeanUtils.copyProperties(eachLike, eachLikeDTO);
				allUserLikesDTOList.add(eachLikeDTO);
			}
			response.setLikesList(allUserLikesDTOList);
		} catch (Exception e) {
			logger.error("error in listAllLikesBasedOnlikedToUserId: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Likes Based On likedToUserId returned successfully", response);
        return response;
	}


	public LikesResponse getLikesDetail(Principal principal, LikesRequest LikesRequest) {
		LikesResponse response = new LikesResponse();
		Likes eachLike = likesDao.findById(LikesRequest.getLikesDTO().getId());
		LikesDTO eachLikeDTO = new LikesDTO();
		BeanUtils.copyProperties(eachLike, eachLikeDTO);
		response.setLikesDTO(eachLikeDTO);
    	return generateSuccessResponse("Like Details retrieved successfully", response);
	}

	
	public LikesResponse generateFailResponse(String msg, List<LikesDTO> savedLikesList, LikesDTO savedLike) {
		LikesResponse response = new LikesResponse();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		response.setLikesList(savedLikesList);
		response.setLikesDTO(savedLike);
		return response;
	}
	
	public LikesResponse generateSuccessResponse(String msg, LikesResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}


}
