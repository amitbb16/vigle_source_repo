package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.CommentsDTO;


public class CommentsResponse extends PaginationGenericResponse {

	
	//private Attributes
	////////////////////
	
	private CommentsDTO comments;
	private List<CommentsDTO> commentsList;
	
	
	//public Attributes
	////////////////////

	public CommentsDTO getComments() {
		return comments;
	}

	public void setComments(CommentsDTO comments) {
		this.comments = comments;
	}

	public List<CommentsDTO> getCommentsList() {
		return commentsList;
	}

	public void setCommentsList(List<CommentsDTO> commentsList) {
		this.commentsList = commentsList;
	}
	

}
