package com.retro.dev.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comments {
    
	//private Attributes
	/////////////////////
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long commentId;

    private Long serviceId;
    
    private Long orderId;
    
    private Long feedId;
    
    private String comment;

    @Column(columnDefinition = "varchar(1) default 'N'")
    private String isLiked;
    
    @Column(columnDefinition = "varchar(1) default 'Y'")
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

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
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
