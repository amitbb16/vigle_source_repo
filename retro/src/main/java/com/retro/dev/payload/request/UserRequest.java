package com.retro.dev.payload.request;

import com.retro.dev.dtos.UserDto;


public class UserRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private UserDto userRequest;

	
	
	//public Attributes
	////////////////////

	public UserDto getUserRequest() {
		return userRequest;
	}

	public void setUserRequest(UserDto userRequest) {
		this.userRequest = userRequest;
	}
	



}
