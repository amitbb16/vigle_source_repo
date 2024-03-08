package com.retro.dev.models;

import java.time.LocalDateTime;

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
@Table(name = "likes")
public class Likes {
    
	//private Attributes
	/////////////////////
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long likedByUserId;

    private Long likedToServiceId;
    
    private Long likedToOrderId;
    
    private Long likedToFeedsId;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;
	
    private Long likedToUserId;
    private String isLikeToComment;
    private Long likedOnCommentId;
    private Long commentedOnUserId;


	//public Attributes
	/////////////////////
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLikedByUserId() {
		return likedByUserId;
	}

	public void setLikedByUserId(Long likedByUserId) {
		this.likedByUserId = likedByUserId;
	}

	public Long getLikedToServiceId() {
		return likedToServiceId;
	}

	public void setLikedToServiceId(Long likedToServiceId) {
		this.likedToServiceId = likedToServiceId;
	}

	public Long getLikedToOrderId() {
		return likedToOrderId;
	}

	public void setLikedToOrderId(Long likedToOrderId) {
		this.likedToOrderId = likedToOrderId;
	}

	public Long getLikedToFeedsId() {
		return likedToFeedsId;
	}

	public void setLikedToFeedsId(Long likedToFeedsId) {
		this.likedToFeedsId = likedToFeedsId;
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

	public Long getLikedToUserId() {
		return likedToUserId;
	}

	public void setLikedToUserId(Long likedToUserId) {
		this.likedToUserId = likedToUserId;
	}

	public String getIsLikeToComment() {
		return isLikeToComment;
	}

	public void setIsLikeToComment(String isLikeToComment) {
		this.isLikeToComment = isLikeToComment;
	}

	public Long getLikedOnCommentId() {
		return likedOnCommentId;
	}

	public void setLikedOnCommentId(Long likedOnCommentId) {
		this.likedOnCommentId = likedOnCommentId;
	}

	public Long getCommentedOnUserId() {
		return commentedOnUserId;
	}

	public void setCommentedOnUserId(Long commentedOnUserId) {
		this.commentedOnUserId = commentedOnUserId;
	}


    
}
