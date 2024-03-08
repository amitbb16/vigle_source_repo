package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.BusinessServicesDTO;


public class BusinessServicesResponse extends PaginationGenericResponse {

	
	//private Attributes
	////////////////////
	
	private String status;
	private String respDesc;
	
	private BusinessServicesDTO businessServices;
	
	private List<String> base64mediaFiles;
	
	//public Attributes
	////////////////////
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BusinessServicesDTO getBusinessServices() {
		return businessServices;
	}

	public void setBusinessServices(BusinessServicesDTO businessServices) {
		this.businessServices = businessServices;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public List<String> getBase64mediaFiles() {
		return base64mediaFiles;
	}

	public void setBase64mediaFiles(List<String> base64mediaFiles) {
		this.base64mediaFiles = base64mediaFiles;
	}
	

}
