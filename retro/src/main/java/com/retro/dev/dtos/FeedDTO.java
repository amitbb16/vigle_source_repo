package com.retro.dev.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FeedDTO {
	
	//private Attributes
	/////////////////////
	
    private Long feedId;

    private Long userId;

    private String userName;
    
    private String name;

    private String profileImage;

    private String visibility;

    private String type;
    
    private String feedData;
    
	private double longitude;
	
	private double latitude;

	private Long likesCount;
	
	private Long commentsCount;

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}	

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;

	private List<FeedMediaLinkDTO> feedMediaLinks;

	public Long getFeedId() {
		return feedId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getType() {
		return type;
	}

	public String getFeedData() {
		return feedData;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public List<FeedMediaLinkDTO> getFeedMediaLinks() {
		return feedMediaLinks;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFeedData(String feedData) {
		this.feedData = feedData;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public void setFeedMediaLinks(List<FeedMediaLinkDTO> feedMediaLinks) {
		this.feedMediaLinks = feedMediaLinks;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Long getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(Long likesCount) {
		this.likesCount = likesCount;
	}

	public Long getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Long commentsCount) {
		this.commentsCount = commentsCount;
	}
}
