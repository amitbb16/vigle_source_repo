package com.retro.dev.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.retro.dev.payload.request.FeedRequest;
import com.retro.dev.payload.response.FeedResponse;
import com.retro.dev.security.services.FeedServices;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    FeedServices feedService;

    @PostMapping(value = "/addFeed", consumes = { MediaType.APPLICATION_JSON_VALUE ,
    												MediaType.MULTIPART_FORM_DATA_VALUE })
    public FeedResponse addFeed(Principal principal, @RequestPart("feedRequest") FeedRequest feedRequest, @RequestPart("feedMedia") List<MultipartFile> feedMedia) {
    	if(null!=feedMedia && feedMedia.size()>0) {
    		feedRequest.setFeedMedia(feedMedia);
    	}
        return feedService.addFeed(principal.getName(), feedRequest);
    }

    @PostMapping("/getAllFeedByUser")
    public FeedResponse getAllFeedByUser(Principal principal, @RequestBody FeedRequest feedRequest) {
        return feedService.getAllFeedByUser(principal, feedRequest);
    }
    
    @PostMapping("/getFeedsByUserToBeDisplayed")
    public FeedResponse getFeedsByUserToBeDisplayed(Principal principal, @RequestBody FeedRequest feedRequest) {
        return feedService.getFeedsByUserToBeDisplayed(principal, feedRequest);
    }
    
    @PostMapping("/getFeedLikesCount")
    public FeedResponse getFeedLikesCount(Principal principal, @RequestBody FeedRequest feedRequest) {
        return feedService.getFeedLikesCount(principal, feedRequest);
    }
    
    @PostMapping("/getFeedComments")
    public FeedResponse getFeedComments(Principal principal, @RequestBody FeedRequest feedRequest) {
        return feedService.getFeedComments(principal, feedRequest);
    }

    @PostMapping("/getLikedBy")
    public FeedResponse getLikedBy(Principal principal, @RequestBody FeedRequest feedRequest) {
        return feedService.getLikedBy(principal, feedRequest);
    }

	
	@PostMapping("/updateFeed") 
	public FeedResponse updateFeed(Principal principal, @RequestBody FeedRequest feedRequest) { 
		return feedService.updateFeed(principal.getName(), feedRequest); 
	}
	  
	  
	@PostMapping("/deleteFeed") 
	public FeedResponse deleteFeed(Principal principal, @RequestBody FeedRequest feedRequest) { 
		return feedService.deleteFeed(principal.getName(), feedRequest); 
	}
	 
	@PostMapping("/deleteFeedMedia") 
	public FeedResponse deleteFeedMedia(Principal principal, @RequestBody FeedRequest feedRequest) { 
		return feedService.deleteFeedMedia(principal.getName(), feedRequest); 
	}

}
