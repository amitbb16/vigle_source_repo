package com.retro.dev.security.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import org.springframework.web.multipart.MultipartFile;

import com.retro.dev.dtos.CommentsDTO;
import com.retro.dev.dtos.FeedDTO;
import com.retro.dev.dtos.FeedMediaLinkDTO;
import com.retro.dev.dtos.UserDto;
import com.retro.dev.models.Comments;
import com.retro.dev.models.Feed;
import com.retro.dev.models.FeedMediaLink;
import com.retro.dev.models.User;
import com.retro.dev.payload.request.FeedRequest;
import com.retro.dev.payload.response.FeedResponse;
import com.retro.dev.repository.CommentsRepository;
import com.retro.dev.repository.FeedMediaLinkRepository;
import com.retro.dev.repository.FeedRepository;
import com.retro.dev.repository.LikesRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;



@Service
public class FeedServices {
    
	@Autowired
	FeedRepository feedDao;
    
	@Autowired
	FeedMediaLinkRepository feedMediaLinkDao;
	
	@Autowired
	CommentsRepository commentsDao;
	
	@Autowired
	LikesRepository likesDao;
	
	@Autowired
	UserRepository userDao;
	
	@Autowired
    DevUtils utils;

    private static final Logger logger = LoggerFactory.getLogger(FeedServices.class);
    public FeedResponse addFeed(String name, FeedRequest request) {
    	FeedResponse response = new FeedResponse();
    	Feed feed = new Feed();
    	try {
    		response = validateFeedRequest(request, PublicConstants.ADD);
    		if(StringUtils.isNotEmpty(response.getResponseStatus())&&
    				response.getResponseStatus().equalsIgnoreCase(PublicConstants.FAIL)) {
    			return response;
    		}
    		BeanUtils.copyProperties(request.getFeed(), feed);
        	feed.setCreatedDate(LocalDateTime.now());
        	feed.setLastUpdatedDate(LocalDateTime.now());
    		Long userId = feed.getUserId();
    		if(userId!=null) {    			
    			feed = feedDao.save(feed);
    			if(null != request.getFeedMedia() && null != feed) {
    				String fileUploadDir = utils.getFileUploadDir()+userId+File.separator+"feed";
			    	List<FeedMediaLinkDTO> feedMediaLinkList = new ArrayList<FeedMediaLinkDTO>();
			    	for(MultipartFile eachMediaFile: request.getFeedMedia()) {
    			    	FeedMediaLink feedMediaLink = new FeedMediaLink();
    			    	FeedMediaLinkDTO feedMediaLinkDto = new FeedMediaLinkDTO();
    					if(eachMediaFile.getName().contains(".."))
    		                throw new FileNotFoundException("Sorry! one of the File names contains invalid path sequence " + eachMediaFile.getName());
    					String originalFileName = eachMediaFile.getOriginalFilename();
    					String newFilePath = createResourceSubFolder(fileUploadDir);
    					File newFeedMediaImgFile = new File(newFilePath+File.separator+originalFileName);
    		            logger.debug("file getting created: "+newFeedMediaImgFile.getPath());
    		            eachMediaFile.transferTo(Paths.get(newFilePath+File.separator+originalFileName));
    		            feedMediaLink.setFeedId(feed.getFeedId());
    		            feedMediaLink.setMediaLink(fileUploadDir+File.separator+originalFileName);
    		            feedMediaLink = feedMediaLinkDao.save(feedMediaLink);
    		    		BeanUtils.copyProperties(feedMediaLink, feedMediaLinkDto);
    		    		feedMediaLink.setCreatedDate(LocalDateTime.now());
    		    		feedMediaLink.setLastUpdatedDate(LocalDateTime.now());
    		            feedMediaLinkList.add(feedMediaLinkDto);
    		         }
    			}
    		} else {
    			return generateFailResponse("user no more available.");
    		}
    	} catch (BeansException e) {
    		logger.error("Feed save failed: could not get feed details");
    		e.printStackTrace();    	
	    } catch (FileNotFoundException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Feed save failed: could not save feed medias");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Feed save failed: invalid path in feed medias");
			e.printStackTrace();
		} catch (IOException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Feed save failed: error in saving feed medias");
			e.printStackTrace();
		}
    	BeanUtils.copyProperties(feed, request.getFeed());
    	response.setFeed(request.getFeed());
    	return generateSuccessResponse("Feed saved successfully", response);
    }
    
    private String createResourceSubFolder(String folderName) throws URISyntaxException, IOException {		
    	String currentDirectory = System.getProperty("user.dir")+File.separator+ folderName+File.separator;
    	logger.debug("file parent directory: "+currentDirectory+File.separator+ folderName+File.separator);
    	File testResultsFolder = new File(currentDirectory);
    	if (!testResultsFolder.exists()) {
	    	Files.createDirectories(Paths.get(testResultsFolder+File.separator));
	    }
    	return currentDirectory;
	}

    
    private FeedResponse validateFeedRequest(FeedRequest request, String action) {
    	FeedResponse response = new FeedResponse();
    	if(action == PublicConstants.ADD) {
        	if(request.getFeed().getUserId() == null) {
        		return generateFailResponse("Please provide a user id.");
        	}
        	
    		if(StringUtils.isBlank(request.getFeed().getVisibility())) {
    			return generateFailResponse("Please provide a visibility.");
    		}
    		
    		if(StringUtils.isBlank(request.getFeed().getType())) {
    			return generateFailResponse("Please provide a type.");
    		}
    	}
    	if(StringUtils.isBlank(request.getFeed().getFeedData()) && request.getFeedMedia() == null) {
    		return generateFailResponse("Please provide a data or upload media to post.");
    	}
		return response;
	}
    
	public FeedResponse generateFailResponse(String msg) {
		FeedResponse response = new FeedResponse();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		return response;
	}
	
	public FeedResponse generateSuccessResponse(String msg, FeedResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}
	
	public FeedResponse getAllFeedByUser(Principal principal,FeedRequest request) {
		FeedResponse response = new FeedResponse();
		try {
			List<Feed> allFeedDataList = feedDao.findAllByUserId(request.getFeed().getUserId());
			if(null!=allFeedDataList && allFeedDataList.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<FeedDTO> allFeedList = new ArrayList<FeedDTO>();
			for(Feed eachFeed : allFeedDataList) {
				FeedDTO eachFeedDTO = new FeedDTO();
				BeanUtils.copyProperties(eachFeed, eachFeedDTO);
				User userEntity = userDao.getOne(eachFeed.getUserId());
				eachFeedDTO.setUserName(userEntity.getUsername());
				eachFeedDTO.setName(userEntity.getName());
				if(!StringUtils.isEmpty(userEntity.getProfileImage())) {
					byte[] profileImgfileContent = FileUtils.readFileToByteArray(new File(System.getProperty("user.dir")+File.separator+ userEntity.getProfileImage()));
					String encodedStringForProfImg = Base64.getEncoder().encodeToString(profileImgfileContent);
					eachFeedDTO.setProfileImage(encodedStringForProfImg);
				}
				
				Long likesCount = likesDao.getFeedLikesCount(eachFeed.getFeedId());
				eachFeedDTO.setLikesCount(likesCount);				
				
				List<Comments> comments = commentsDao.getFeedComments(eachFeed.getFeedId());
				if(comments !=null && comments.size() > 0) {		
					eachFeedDTO.setCommentsCount(Long.parseLong(comments.size()+""));
				}

				allFeedList.add(eachFeedDTO);
			}
			response.setTotalCount(allFeedList.size());
			response.setFeedList(allFeedList);
		} catch (Exception e) {
			logger.error("error in listAllFeed: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Feed returned successfully", response);
        return response;
	}
	
	public FeedResponse getFeedsByUserToBeDisplayed(Principal principal,FeedRequest request) {
		FeedResponse response = new FeedResponse();
		try {
			List<Feed> allFeedDataList = feedDao.findAllByUserId(request.getFeed().getUserId());
			if(null!=allFeedDataList && allFeedDataList.size()<1) {
				return generateSuccessResponse("No records found", response);
			}
			List<FeedDTO> allFeedList = new ArrayList<FeedDTO>();
			for(Feed eachFeed : allFeedDataList) {
				FeedDTO eachFeedDTO = new FeedDTO();
				BeanUtils.copyProperties(eachFeed, eachFeedDTO);
				User userEntity = userDao.getOne(eachFeed.getUserId());
				eachFeedDTO.setUserName(userEntity.getUsername());
				eachFeedDTO.setName(userEntity.getName());
				if(!StringUtils.isEmpty(userEntity.getProfileImage())) {
					byte[] profileImgfileContent = FileUtils.readFileToByteArray(new File(System.getProperty("user.dir")+File.separator+ userEntity.getProfileImage()));
					String encodedStringForProfImg = Base64.getEncoder().encodeToString(profileImgfileContent);
					eachFeedDTO.setProfileImage(encodedStringForProfImg);
				}
				
				Long likesCount = likesDao.getFeedLikesCount(eachFeed.getFeedId());
				eachFeedDTO.setLikesCount(likesCount);				
				
				List<Comments> comments = commentsDao.getFeedComments(eachFeed.getFeedId());
				if(comments !=null && comments.size() > 0) {	
					eachFeedDTO.setCommentsCount(Long.parseLong(comments.size()+""));
				}

				allFeedList.add(eachFeedDTO);
			}
			response.setFeedList(allFeedList);
		} catch (Exception e) {
			logger.error("error in listAllFeed: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("List of All Feed returned successfully", response);
        return response;
	}

	public FeedResponse getFeedLikesCount(Principal principal,FeedRequest request) {
		FeedResponse response = new FeedResponse();
		try {
			Long likesCount = likesDao.getFeedLikesCount(request.getFeed().getFeedId());
			response.setLikesCount(likesCount);
		} catch (Exception e) {
			logger.error("error in getFeedLikesCount: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("Feed likes counts returned successfully", response);
        return response;
	}
	
	public FeedResponse getFeedComments(Principal principal,FeedRequest request) {
		FeedResponse response = new FeedResponse();
		try {
			List<Comments> comments = commentsDao.getFeedComments(request.getFeed().getFeedId());
			List<CommentsDTO> commentsList = new ArrayList<>();
			if(comments !=null && comments.size() > 0) {				
				response.setTotalCount(comments.size());
				comments.stream().forEach( commentEntity -> {
					CommentsDTO eachDto = new CommentsDTO();
					BeanUtils.copyProperties(commentEntity, eachDto);
					commentsList.add(eachDto);
				});
				response.setCommentsList(commentsList);
			}
			
		} catch (Exception e) {
			logger.error("error in getFeedCommentsCount: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("Feed comments counts returned successfully", response);
        return response;
	}

	public FeedResponse getLikedBy(Principal principal,FeedRequest request) {
		FeedResponse response = new FeedResponse();
		try {
			List<Long> likedBy = likesDao.getFeedLikedBy(request.getFeed().getFeedId());
			List<User> userList = userDao.findByIds(likedBy);
			List<UserDto> userDtoList = new ArrayList<UserDto>();
			if(userList != null && userList.size() > 0) {
				for(User eachUserd : userList) {
					UserDto eachDTO = new UserDto();
					BeanUtils.copyProperties(eachUserd, eachDTO);
					userDtoList.add(eachDTO);
				}
				response.setTotalCount(userList.size());
				response.setLikedBy(userDtoList);
			}
		} catch (Exception e) {
			logger.error("error in getLikedBy: "+e.getMessage());
			e.printStackTrace();
		}
		response = generateSuccessResponse("Feed liked by returned successfully", response);
        return response;
	}

    public FeedResponse updateFeed(String name, FeedRequest request) {
    	FeedResponse response = new FeedResponse();
    	try {
    		response = validateFeedRequest(request, PublicConstants.UPDATE);
    		if(StringUtils.isNotEmpty(response.getResponseStatus())&&
    				response.getResponseStatus().equalsIgnoreCase(PublicConstants.FAIL)) {
    			return response;
    		}
			Optional<Feed> feed = feedDao.findById(request.getFeed().getFeedId());
			if(null != feed && !request.getFeed().getFeedData().isEmpty()) {
				feed.get().setFeedData(request.getFeed().getFeedData());
				feed.get().setLastUpdatedDate(LocalDateTime.now());
				feedDao.save(feed.get());
			}
			
			if(null != request.getFeedMedia()) {
				List<FeedMediaLink> feedLinks = feedMediaLinkDao.findAllById(request.getFeed().getFeedId());
				// delete old media
				if(feedLinks != null && feedLinks.size() > 0) {
					for(FeedMediaLink link : feedLinks) {
						feedMediaLinkDao.delete(link);
					}
				}
				
				// add new media
				String fileUploadDir = utils.getFileUploadDir()+feed.get().getUserId()+File.separator+"feed";
		    	List<FeedMediaLinkDTO> feedMediaLinkList = new ArrayList<FeedMediaLinkDTO>();
		    	for(MultipartFile eachMediaFile: request.getFeedMedia()) {
			    	FeedMediaLink feedMediaLink = new FeedMediaLink();
			    	FeedMediaLinkDTO feedMediaLinkDto = new FeedMediaLinkDTO();
					if(eachMediaFile.getName().contains(".."))
		                throw new FileNotFoundException("Sorry! one of the File names contains invalid path sequence " + eachMediaFile.getName());
					String originalFileName = eachMediaFile.getOriginalFilename();
					String newFilePath = createResourceSubFolder(fileUploadDir);
					File newFeedMediaImgFile = new File(newFilePath+File.separator+originalFileName);
		            logger.debug("file getting created: "+newFeedMediaImgFile.getPath());
		            eachMediaFile.transferTo(Paths.get(newFilePath+File.separator+originalFileName));
		            feedMediaLink.setFeedId(feed.get().getFeedId());
		            feedMediaLink.setMediaLink(fileUploadDir+File.separator+originalFileName);
		            feedMediaLink = feedMediaLinkDao.save(feedMediaLink);
		    		BeanUtils.copyProperties(feedMediaLink, feedMediaLinkDto);
		            feedMediaLinkList.add(feedMediaLinkDto);
		         }
			}
			
			BeanUtils.copyProperties(feed.get(), request.getFeed());
	    	response.setFeed(request.getFeed());

		} catch (BeansException e) {
    		logger.error("Feed update failed: could not get feed details");
    		e.printStackTrace();    	
	    } catch (FileNotFoundException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Feed update failed: could not save feed medias");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Feed update failed: invalid path in feed medias");
			e.printStackTrace();
		} catch (IOException e) {
			response.setStatus(PublicConstants.FAIL);
			response.setRespDesc("Feed update failed: error in saving feed medias");
			e.printStackTrace();
		}
    	return generateSuccessResponse("Feed update successfully", response);
    }
    
	public FeedResponse deleteFeed(String name, FeedRequest request) {
		FeedResponse response = new FeedResponse();
		Optional<Feed> feed = feedDao.findById(request.getFeed().getFeedId());
		if(null==feed) {
			return generateFailResponse("Feed not found");
		} else {
			List<FeedMediaLink> feedLinks = feedMediaLinkDao.findAllById(request.getFeed().getFeedId());
			if(feedLinks != null && feedLinks.size() > 0) {
				for(FeedMediaLink link : feedLinks) {
					feedMediaLinkDao.delete(link);
				}
			} 

			feedDao.delete(feed.get());
			response.setFeed(request.getFeed());
	    	return generateSuccessResponse("Feed removed successfully", response);
		}
    }
	
	public FeedResponse deleteFeedMedia(String name, FeedRequest request) {
		FeedResponse response = new FeedResponse();
		if(request.getFeedMediaLinkIds() != null && request.getFeedMediaLinkIds().size() > 0) {
			for(Long id : request.getFeedMediaLinkIds()) {
				Optional<FeedMediaLink> feedLink = feedMediaLinkDao.findById(id);
				if(feedLink != null) {
					feedMediaLinkDao.delete(feedLink.get());
				}
			}
		}
    	return generateSuccessResponse("Feed media removed successfully", response);		
    }

}
