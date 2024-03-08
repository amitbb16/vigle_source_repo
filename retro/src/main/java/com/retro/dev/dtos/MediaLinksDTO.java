package com.retro.dev.dtos;

import java.util.List;

public class MediaLinksDTO {
	private String type;
	private String serviceId;
	private Long categoryId;
	private List<BusinessServicesDTO> serviceList;
	private List<MediaLinksDTO> list;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getServiceId() {
		return serviceId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public List<BusinessServicesDTO> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<BusinessServicesDTO> serviceList) {
		this.serviceList = serviceList;
	}
	public List<MediaLinksDTO> getList() {
		return list;
	}
	public void setList(List<MediaLinksDTO> list) {
		this.list = list;
	}
}
