package com.retro.dev.payload.request;

public class PaginationGenericRequest {

	
	//private Attributes
	////////////////////
	
	private String requestId;
	private int pageNo;
	private int pageSize;
	private String sortValue;
	private String sortType;
	
	//public Attributes
	////////////////////
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public int getPageNo() {
		/*if(pageNo == 0) {
			pageNo = 1;
		}*/
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		if(pageSize == 0) {
			pageSize = 10;
		}
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSortValue() {
		return sortValue;
	}
	public void setSortValue(String sortValue) {
		this.sortValue = sortValue;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
}
