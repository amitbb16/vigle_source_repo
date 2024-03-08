package com.retro.dev.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FeedMediaLinkDTO {
	
	//private Attributes
	/////////////////////
	
    private Long feedMediaLinkId;
	
    private Long feedId;

    private String mediaLink;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;

	public Long getFeedMediaLinkId() {
		return feedMediaLinkId;
	}

	public Long getFeedId() {
		return feedId;
	}

	public String getMediaLink() {
		return mediaLink;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setFeedMediaLinkId(Long feedMediaLinkId) {
		this.feedMediaLinkId = feedMediaLinkId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

	public void setMediaLink(String mediaLink) {
		this.mediaLink = mediaLink;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
}
