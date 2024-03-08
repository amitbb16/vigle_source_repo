package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.UserDto;

public class UserResponse extends PaginationGenericResponse{

    

	//private attributes
    ////////////////////
    private List<UserDto> usersList;
    private UserDto user;
    private int OrdersDone;
    private float rating;
    
    //public attributes
    ////////////////////
	public List<UserDto> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<UserDto> usersList) {
		this.usersList = usersList;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public int getOrdersDone() {
		return OrdersDone;
	}

	public void setOrdersDone(int ordersDone) {
		OrdersDone = ordersDone;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
	
}
