package com.retro.dev.dtos;

import java.util.List;

public class GalleryDTO {
	
	private String responseStatus;
	private List<MediaLinksDTO> data;	
	
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public List<MediaLinksDTO> getData() {
		return data;
	}
	public void setData(List<MediaLinksDTO> data) {
		this.data = data;
	}
}
