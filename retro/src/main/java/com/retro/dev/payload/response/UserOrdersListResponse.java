package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.BusinessOffersListDTO;
import com.retro.dev.dtos.UserOrdersListDTO;

public class UserOrdersListResponse extends PaginationGenericResponse{

    

	//private attributes
    ////////////////////
    private List<UserOrdersListDTO> pendingOrdersList;
	private List<UserOrdersListDTO> activeOrdersList;
    private List<UserOrdersListDTO> completedOrdersList;
    private List<UserOrdersListDTO> discountedOffersList;
    
    //public attributes
    ////////////////////
    public List<UserOrdersListDTO> getPendingOrdersList() {
		return pendingOrdersList;
	}
	public void setPendingOrdersList(List<UserOrdersListDTO> pendingOrdersList) {
		this.pendingOrdersList = pendingOrdersList;
	}
    public List<UserOrdersListDTO> getActiveOrdersList() {
		return activeOrdersList;
	}
	public void setActiveOrdersList(List<UserOrdersListDTO> activeOrdersList) {
		this.activeOrdersList = activeOrdersList;
	}
	public List<UserOrdersListDTO> getCompletedOrdersList() {
		return completedOrdersList;
	}
	public void setCompletedOrdersList(List<UserOrdersListDTO> completedOrdersList) {
		this.completedOrdersList = completedOrdersList;
	}
	public List<UserOrdersListDTO> getDiscountedOffersList() {
		return discountedOffersList;
	}
	public void setDiscountedOffersList(List<UserOrdersListDTO> discountedOffersList) {
		this.discountedOffersList = discountedOffersList;
	}
    
}
