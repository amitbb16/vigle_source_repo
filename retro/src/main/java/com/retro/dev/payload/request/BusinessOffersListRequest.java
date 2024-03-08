package com.retro.dev.payload.request;

import com.retro.dev.dtos.BusinessOffersListDTO;


public class BusinessOffersListRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private BusinessOffersListDTO businessOffers;

	
	
	//public Attributes
	////////////////////
	
	public BusinessOffersListDTO getBusinessOffers() {
		return businessOffers;
	}

	public void setBusinessOffers(BusinessOffersListDTO businessOffers) {
		this.businessOffers = businessOffers;
	}
	



}
