package com.retro.dev.payload.request;

import com.retro.dev.dtos.ServiceRequestDTO;


public class ServiceRequestRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private ServiceRequestDTO serviceReq;
	
	//dafault constructor
	////////////////////
	public ServiceRequestRequest() {
    	
	}
    
	//public Attributes
	////////////////////

	public ServiceRequestDTO getServiceReq() {
		return serviceReq;
	}

	public void setServiceReq(ServiceRequestDTO serviceReq) {
		this.serviceReq = serviceReq;
	}
	

}
