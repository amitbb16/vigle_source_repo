package com.retro.dev.payload.request;

import com.retro.dev.dtos.LikesDTO;


public class LikesRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private LikesDTO likesDTO;
	
	
	
	//public Attributes
	////////////////////

	public LikesDTO getLikesDTO() {
		return likesDTO;
	}

	public void setLikesDTO(LikesDTO likesDTO) {
		this.likesDTO = likesDTO;
	}

}
