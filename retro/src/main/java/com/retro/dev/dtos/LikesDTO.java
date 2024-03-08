package com.retro.dev.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;



public class LikesDTO {
    
	//private Attributes
	/////////////////////
    private Long id;
    private Long userId;
    private Long serviceId;
    private Long orderId;
    private Long feedsId;
    private String comment;
    private String isLiked;
    private String isCommented;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;
	
    private Long commentedOnUserId;
    private Long likedToUserId;
    private String isReplyComment;

	
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

	public Long getFeedsId() {
		return feedsId;
	}

	public void setFeedsId(Long feedsId) {
		this.feedsId = feedsId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIsCommented() {
		return isCommented;
	}

	public void setIsCommented(String isCommented) {
		this.isCommented = isCommented;
	}

	public Long getCommentedOnUserId() {
		return commentedOnUserId;
	}

	public void setCommentedOnUserId(Long commentedOnUserId) {
		this.commentedOnUserId = commentedOnUserId;
	}

	public Long getLikedToUserId() {
		return likedToUserId;
	}

	public void setLikedToUserId(Long likedToUserId) {
		this.likedToUserId = likedToUserId;
	}

	public String getIsReplyComment() {
		return isReplyComment;
	}

	public void setIsReplyComment(String isReplyComment) {
		this.isReplyComment = isReplyComment;
	}

    
}
