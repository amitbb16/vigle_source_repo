package com.retro.dev.payload.request;

import com.retro.dev.dtos.CommentsDTO;


public class CommentsRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private CommentsDTO comments;
	
	
	
	//public Attributes
	////////////////////

	public CommentsDTO getComments() {
		return comments;
	}

	public void setComments(CommentsDTO comments) {
		this.comments = comments;
	}

}
