package com.retro.dev.payload.response;

public class PaginationGenericResponse {

	
	//private Attributes
	////////////////////
	
	private String requestId;
	
	private String responseStatus;
	private String responseDesc;
	
	private int totalCount;
	
	
	
	//public Attributes
	////////////////////
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getResponseDesc() {
		return responseDesc;
	}
	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}
	
	
}
