package com.retro.dev.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;



public class FavouritesDTO {
    
	//private Attributes
	/////////////////////
	
    private Long id;
    private Long userId;
    private Long serviceId;
    private Long orderId;
    private String isLiked;	
    private String isFavorite;
    private Long favouriteUserId;
    private Long feedId;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;
	private String reviewMediaLinks;

	
	//public Attributes
	/////////////////////
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(String isLiked) {
		this.isLiked = isLiked;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getFavouriteUserId() {
		return favouriteUserId;
	}

	public void setFavouriteUserId(Long favouriteUserId) {
		this.favouriteUserId = favouriteUserId;
	}

	public String getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(String isFavorite) {
		this.isFavorite = isFavorite;
	}

	public String getReviewMediaLinks() {
		return reviewMediaLinks;
	}

	public void setReviewMediaLinks(String reviewMediaLinks) {
		this.reviewMediaLinks = reviewMediaLinks;
	}

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

    
    
}
