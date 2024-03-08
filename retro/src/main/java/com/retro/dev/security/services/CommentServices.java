package com.retro.dev.security.services;

import java.io.File;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.CommentsDTO;
import com.retro.dev.dtos.UserDto;
import com.retro.dev.models.BusinessServices;
import com.retro.dev.models.Comments;
import com.retro.dev.models.Feed;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.CommentsRequest;
import com.retro.dev.payload.response.CommentsResponse;
import com.retro.dev.repository.BusinessServicesRepository;
import com.retro.dev.repository.CommentsRepository;
import com.retro.dev.repository.FeedRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.PublicConstants;



@Service
public class CommentServices {
    
	@Autowired
	CommentsRepository commentsDao;
    
	@Autowired
	BusinessServicesRepository businessServiceDao;
    
	@Autowired
    UserRepository userRepository;
    
	@Autowired
	UserRepository userDao;
	
	@Autowired
	FeedRepository feedDao;
    
	
    private static final Logger logger = LoggerFactory.getLogger(CommentServices.class);



    public CommentsResponse addComment(String name, CommentsRequest commentsRequest, String action) {
    	CommentsResponse response = new CommentsResponse();
    	Comments comment = new Comments();
    	try {
    		response = validateCommentsRequest(commentsRequest);
    		if(StringUtils.isNotEmpty(response.getResponseStatus())&&
    				response.getResponseStatus().equalsIgnoreCase(PublicConstants.FAIL)) {
    			return response;
    		}
    		BeanUtils.copyProperties(commentsRequest.getComments(), comment);
    		if(action.equalsIgnoreCase("add")) {
        		comment.setCreatedDate(LocalDateTime.now());
    		}
    		comment.setLastUpdatedDate(LocalDateTime.now());
    		Long targetUserId = comment.getCommentedOnUserId()!=null?comment.getCommentedOnUserId() :
    		comment.getLikedToUserId()!=null?comment.getLikedToUserId() : retrieveTargetUserId(comment);
    		if(targetUserId!=null) {
    			if (commentsRequest.getComments().getIsCommented().equalsIgnoreCase("Y")) {
    				comment.setCommentedOnUserId(targetUserId);
    			}
    			if (commentsRequest.getComments().getIsLiked().equalsIgnoreCase("Y")) {
    				comment.setLikedToUserId(targetUserId);
    			}
    			comment = commentsDao.save(comment);
    		} else {
    			return generateFailResponse("user no more available.", null, null);
    		}
    	} catch (BeansException e) {
    		logger.error("failed to save comment");
    		e.printStackTrace();
    	}
    	BeanUtils.copyProperties(comment, commentsRequest.getComments());
    	response.setComments(commentsRequest.getComments());
    	return generateSuccessResponse("Comments saved successfully", response);
    }


    private CommentsResponse validateCommentsRequest(CommentsRequest commentsRequest) {
    	CommentsResponse response = new CommentsResponse();
    	if(commentsRequest.getComments().getServiceId() ==null && commentsRequest.getComments().getOrderId() ==null 
    			&& commentsRequest.getComments().getFeedId() ==null) {
    		return generateFailResponse("Please provide either serviceId or orderId or feedId.", null, null);
    	}
    	if(StringUtils.isNotBlank(commentsRequest.getComments().getIsCommented()) && 
    			commentsRequest.getComments().getIsCommented().equalsIgnoreCase("Y")) {
    		if(StringUtils.isBlank(commentsRequest.getComments().getComment())) {
    			return generateFailResponse("Please provide a proper comment.", null, null);
    		}
    		if(StringUtils.isBlank(commentsRequest.getComments().getIsReplyComment())) {
    			return generateFailResponse("Please provide a proper Y/N value for isReplyComment field.", null, null);
    		}
    		
    	}
		//below condition not required anymore, handled at targetUserId setting
    	/*if(StringUtils.isNotBlank(commentsRequest.getComments().getIsLiked()) && 
    			commentsRequest.getComments().getIsLiked().equalsIgnoreCase("Y")) {
    		if(commentsRequest.getComments().getLikedToUserId()==null) {
    			return generateFailResponse("Please provide a value for likedToUserId.", null, null);
    		}
    	}*/
		return response;
	}


	/****************************************************
     * this method needs to be corrected in future w.r.t.
     * the search Target UserId based on OrderId
     ****************************************************/
    private Long retrieveTargetUserId(Comments comment) {
    	Long userId = null;
    	if(null!=comment.getServiceId()) {
    		Optional<BusinessServices> serviceObj = businessServiceDao.findById(comment.getServiceId());
    		userId = null!=serviceObj? serviceObj.get().getUserId():null;
    	} else if(null!=comment.getOrderId()) {
    		Optional<BusinessServices> serviceObj = businessServiceDao.findById(comment.getOrderId());
    		userId = null!=serviceObj? serviceObj.get().getUserId():null;
    	} else if(null!=comment.getFeedId()) {
    		Optional<Feed> targetUserIdFeed = feedDao.findById(comment.getFeedId());
    		userId = null!=targetUserIdFeed? targetUserIdFeed.get().getUserId():0;
    	}
    	return userId;
    }


	public CommentsResponse updateComment(String name, CommentsRequest commentsRequest) {
		if(null==commentsRequest.getComments().getId()) {
			return generateFailResponse("Please select/provide the comment id to update.", null, null);
		}
		Comments storedComment = commentsDao.findById(commentsRequest.getComments().getId());
		LocalDateTime createdDate = storedComment.getCreatedDate();
		BeanUtils.copyProperties(commentsRequest.getComments(), storedComment);	//adding new request values to storedComment
		BeanUtils.copyProperties(storedComment, commentsRequest.getComments());	//copying merged values to send to service
		commentsRequest.getComments().setCreatedDate(createdDate);
		return addComment(name, commentsRequest, "update");
	}
	

	
	public CommentsResponse removeComment(String name, CommentsRequest commentsRequest) {
		CommentsResponse response = new CommentsResponse();
		Comments comment = commentsDao.findById(commentsRequest.getComments().getId());
		if(null==comment) {
			return generateFailResponse("Comment not found", null, null);
		}
		commentsDao.delete(comment);
		response.setComments(commentsRequest.getComments());
    	return generateSuccessResponse("Comment removed successfully", response);
	}
	
	
	public CommentsResponse listAllComments(Principal principal, CommentsRequest listComments) {
		CommentsResponse response = new CommentsResponse();
		try {
			List<Comments> allCommentsDataList = commentsDao.findAll();
			if(null!=allCommentsDataList && allCommentsDataList.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<CommentsDTO> allCommentsList = new ArrayList<CommentsDTO>();
			for(Comments eachComment : allCommentsDataList) {
				CommentsDTO eachCommentDTO = new CommentsDTO();
				BeanUtils.copyProperties(eachComment, eachCommentDTO);
				allCommentsList.add(eachCommentDTO);
			}
			response.setCommentsList(allCommentsList);
		} catch (Exception e) {
			logger.error("error in listAllComments: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Comments returned successfully", response);
        return response;
	}


	public CommentsResponse listAllCommentsBasedOnUser(Principal principal, CommentsRequest commentsRequest) {
		CommentsResponse response = new CommentsResponse();
		try {
			List<Comments> userCommentsList = commentsDao.findAllByUserId(commentsRequest.getComments().getUserId());
			if(null!=userCommentsList && userCommentsList.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<CommentsDTO> allUserCommentsDTOList = new ArrayList<CommentsDTO>();
			for(Comments eachComment : userCommentsList) {
				CommentsDTO eachCommentDTO = new CommentsDTO();
				BeanUtils.copyProperties(eachComment, eachCommentDTO);
				allUserCommentsDTOList.add(eachCommentDTO);
			}
			response.setCommentsList(allUserCommentsDTOList);
		} catch (Exception e) {
			logger.error("error in listAllCommentsBasedOnUser: "+e.getMessage());
			e.printStackTrace();
		}
		return generateSuccessResponse("List of All User Comments returned successfully", response);
	}


	public CommentsResponse listAllCommentsBasedOnService(Principal principal, CommentsRequest commentsRequest) {
		CommentsResponse response = new CommentsResponse();
		try {
			List<Comments> commentsListBasedOnService = commentsDao.findAllByServiceId(commentsRequest.getComments().getServiceId());
			if(null!=commentsListBasedOnService && commentsListBasedOnService.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<CommentsDTO> allUserCommentsDTOList = new ArrayList<CommentsDTO>();
			for(Comments eachComment : commentsListBasedOnService) {
				CommentsDTO eachCommentDTO = new CommentsDTO();
				BeanUtils.copyProperties(eachComment, eachCommentDTO);
				allUserCommentsDTOList.add(eachCommentDTO);
			}
			response.setCommentsList(allUserCommentsDTOList);
		} catch (Exception e) {
			logger.error("error in listAllCommentsBasedOnService: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Comments Based On Service returned successfully", response);
        return response;
	}

	public CommentsResponse listAllCommentsBasedOnFeed(Principal principal, CommentsRequest commentsRequest) {
		CommentsResponse response = new CommentsResponse();
		try {
			List<Comments> commentsListBasedOnService = commentsDao.findAllByFeedId(commentsRequest.getComments().getFeedId(), commentsRequest.getComments().getIsCommented());
			if(null!=commentsListBasedOnService && commentsListBasedOnService.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<CommentsDTO> allUserCommentsDTOList = new ArrayList<CommentsDTO>();
			for(Comments eachComment : commentsListBasedOnService) {
				CommentsDTO eachCommentDTO = new CommentsDTO();
				UserDto userDto = new UserDto();
				BeanUtils.copyProperties(eachComment, eachCommentDTO);
				User userEntity = userDao.getOne(eachComment.getUserId());
				userDto.setUserName(userEntity.getUsername());
				userDto.setName(userEntity.getName());
				if(!StringUtils.isEmpty(userEntity.getProfileImage())) {
					byte[] profileImgfileContent = FileUtils.readFileToByteArray(new File(System.getProperty("user.dir")+File.separator+ userEntity.getProfileImage()));
					String encodedStringForProfImg = Base64.getEncoder().encodeToString(profileImgfileContent);
					userDto.setProfileImage(encodedStringForProfImg);
				}
				eachCommentDTO.setUserDetails(userDto);
				allUserCommentsDTOList.add(eachCommentDTO);
			}
			response.setCommentsList(allUserCommentsDTOList);
		} catch (Exception e) {
			logger.error("error in listAllCommentsBasedOnService: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Comments Based On Service returned successfully", response);
        return response;
	}

	public CommentsResponse listAllCommentsBasedOnCommentedOnUserId(Principal principal, CommentsRequest commentsRequest) {
		CommentsResponse response = new CommentsResponse();
		try {
			Optional<User> userObj = userRepository.findByUsername(principal.getName());
	    	if(userObj==null||!userObj.isPresent()) {
				return generateFailResponse("UserName not found.", null, null);
			}
			List<Comments> commentsListBasedOnCommentedOnUserId = commentsDao.findByUserIdAndCommentedOnUserId(userObj.get().getId(), 
					commentsRequest.getComments().getCommentedOnUserId());
			if(null!=commentsListBasedOnCommentedOnUserId && commentsListBasedOnCommentedOnUserId.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<CommentsDTO> allUserCommentsDTOList = new ArrayList<CommentsDTO>();
			for(Comments eachComment : commentsListBasedOnCommentedOnUserId) {
				CommentsDTO eachCommentDTO = new CommentsDTO();
				BeanUtils.copyProperties(eachComment, eachCommentDTO);
				allUserCommentsDTOList.add(eachCommentDTO);
			}
			response.setCommentsList(allUserCommentsDTOList);
		} catch (Exception e) {
			logger.error("error in listAllCommentsBasedOnCommentedOnUserId: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Comments Based On CommentedOnUserId returned successfully", response);
        return response;
	}


	public CommentsResponse listAllCommentsBasedOnlikedToUserId(Principal principal, CommentsRequest commentsRequest) {
		CommentsResponse response = new CommentsResponse();
		try {
			Optional<User> userObj = userRepository.findByUsername(principal.getName());
	    	if(userObj==null||!userObj.isPresent()) {
				return generateFailResponse("UserName not found.", null, null);
			}
			List<Comments> commentsListBasedOnLikedToUserId = commentsDao.findByUserIdAndLikedToUserId(userObj.get().getId(), 
					commentsRequest.getComments().getLikedToUserId());
			if(null!=commentsListBasedOnLikedToUserId && commentsListBasedOnLikedToUserId.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<CommentsDTO> allUserCommentsDTOList = new ArrayList<CommentsDTO>();
			for(Comments eachComment : commentsListBasedOnLikedToUserId) {
				CommentsDTO eachCommentDTO = new CommentsDTO();
				BeanUtils.copyProperties(eachComment, eachCommentDTO);
				allUserCommentsDTOList.add(eachCommentDTO);
			}
			response.setCommentsList(allUserCommentsDTOList);
		} catch (Exception e) {
			logger.error("error in listAllCommentsBasedOnlikedToUserId: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Comments Based On likedToUserId returned successfully", response);
        return response;
	}


	public CommentsResponse getCommentsDetail(Principal principal, CommentsRequest commentsRequest) {
		CommentsResponse response = new CommentsResponse();
		Comments eachComment = commentsDao.findById(commentsRequest.getComments().getId());
		CommentsDTO eachCommentDTO = new CommentsDTO();
		BeanUtils.copyProperties(eachComment, eachCommentDTO);
		response.setComments(eachCommentDTO);
    	return generateSuccessResponse("Comment Details retrieved successfully", response);
	}

	
	public CommentsResponse generateFailResponse(String msg, List<CommentsDTO> savedCommentsList, CommentsDTO savedComment) {
		CommentsResponse response = new CommentsResponse();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		response.setCommentsList(savedCommentsList);
		response.setComments(savedComment);
		return response;
	}
	
	public CommentsResponse generateSuccessResponse(String msg, CommentsResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}


}
