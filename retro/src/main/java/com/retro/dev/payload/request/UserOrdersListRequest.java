package com.retro.dev.payload.request;

import com.retro.dev.dtos.UserOrdersListDTO;


public class UserOrdersListRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private UserOrdersListDTO userOrders;

	
	
	//public Attributes
	////////////////////
	
	public UserOrdersListDTO getUserOrders() {
		return userOrders;
	}

	public void setUserOrders(UserOrdersListDTO userOrders) {
		this.userOrders = userOrders;
	}
	



}
