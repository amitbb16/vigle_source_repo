package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.CommentsDTO;
import com.retro.dev.dtos.FeedDTO;
import com.retro.dev.dtos.UserDto;


public class FeedResponse extends PaginationGenericResponse {

	
	//private Attributes
	////////////////////
	
	private FeedDTO feed;
	private List<FeedDTO> feedList;
	private String status;
	private String respDesc;
	private Long likesCount;
	private Long commentsCount;
	private List<CommentsDTO> commentsList;
	private List<UserDto> likedBy;

	//public Attributes
	////////////////////
	
	public FeedDTO getFeed() {
		return feed;
	}
	
	public List<FeedDTO> getFeedList() {
		return feedList;
	}
	
	public void setFeed(FeedDTO feed) {
		this.feed = feed;
	}
	
	public void setFeedList(List<FeedDTO> feedList) {
		this.feedList = feedList;
	}

	public String getStatus() {
		return status;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public Long getLikesCount() {
		return likesCount;
	}

	public Long getCommentsCount() {
		return commentsCount;
	}

	public void setLikesCount(Long likesCount) {
		this.likesCount = likesCount;
	}

	public void setCommentsCount(Long commentsCount) {
		this.commentsCount = commentsCount;
	}

	public List<CommentsDTO> getCommentsList() {
		return commentsList;
	}

	public void setCommentsList(List<CommentsDTO> commentsList) {
		this.commentsList = commentsList;
	}

	public List<UserDto> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(List<UserDto> likedBy) {
		this.likedBy = likedBy;
	}
	
}
