package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.BusinessOffersListDTO;

public class BusinessOffersListResponse extends PaginationGenericResponse{

    
	
	//private attributes
    ////////////////////
    private List<BusinessOffersListDTO> activeOffersList;
    private List<BusinessOffersListDTO> completedOffersList;
    private List<BusinessOffersListDTO> discountedOffersList;
    
    //public attributes
    ////////////////////
    public List<BusinessOffersListDTO> getActiveOffersList() {
    	return activeOffersList;
    }
    public void setActiveOffersList(List<BusinessOffersListDTO> activeOffersList) {
    	this.activeOffersList = activeOffersList;
    }
    public List<BusinessOffersListDTO> getCompletedOffersList() {
    	return completedOffersList;
    }
    public void setCompletedOffersList(List<BusinessOffersListDTO> completedOffersList) {
    	this.completedOffersList = completedOffersList;
    }
	public List<BusinessOffersListDTO> getDiscountedOffersList() {
		return discountedOffersList;
	}
	public void setDiscountedOffersList(List<BusinessOffersListDTO> discountedOffersList) {
		this.discountedOffersList = discountedOffersList;
	}
    
}
