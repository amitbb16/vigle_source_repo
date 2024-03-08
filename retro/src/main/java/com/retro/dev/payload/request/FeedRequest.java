package com.retro.dev.payload.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.retro.dev.dtos.FeedDTO;


public class FeedRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private FeedDTO feed;
	private List<MultipartFile> feedMedia;
    private List<Long> feedMediaLinkIds;

	//public Attributes
	////////////////////

	public FeedDTO getFeed() {
		return feed;
	}

	public void setFeed(FeedDTO feed) {
		this.feed = feed;
	}

	public List<MultipartFile> getFeedMedia() {
		return feedMedia;
	}

	public void setFeedMedia(List<MultipartFile> feedMedia) {
		this.feedMedia = feedMedia;
	}

	public List<Long> getFeedMediaLinkIds() {
		return feedMediaLinkIds;
	}

	public void setFeedMediaLinkIds(List<Long> feedMediaLinkIds) {
		this.feedMediaLinkIds = feedMediaLinkIds;
	}	
	
}
