package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.ReviewDTO;


public class ReviewResponse extends PaginationGenericResponse {

	
	//private Attributes
	////////////////////
	
	private List<ReviewDTO> reviewsList;
	
	
	//public Attributes
	////////////////////
	public List<ReviewDTO> getReviewsList() {
		return reviewsList;
	}

	public void setReviewsList(List<ReviewDTO> reviewsList) {
		this.reviewsList = reviewsList;
	}


}
