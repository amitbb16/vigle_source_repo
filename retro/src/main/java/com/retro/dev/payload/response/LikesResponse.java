package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.LikesDTO;


public class LikesResponse extends PaginationGenericResponse {

	
	//private Attributes
	////////////////////
	
	private LikesDTO likesDTO;
	private List<LikesDTO> likesList;
	
	
	//public Attributes
	////////////////////

	public LikesDTO getLikesDTO() {
		return likesDTO;
	}

	public void setLikesDTO(LikesDTO likesDTO) {
		this.likesDTO = likesDTO;
	}

	public List<LikesDTO> getLikesList() {
		return likesList;
	}

	public void setLikesList(List<LikesDTO> likesList) {
		this.likesList = likesList;
	}
	

}
