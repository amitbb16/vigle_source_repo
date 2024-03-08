package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.SearchDTO;


public class SearchResponse extends PaginationGenericResponse {

	
	//private Attributes
	////////////////////
	
	private List<SearchDTO> searchResultsList;
	
	
	//public Attributes
	////////////////////

	public List<SearchDTO> getSearchResultsList() {
		return searchResultsList;
	}

	public void setSearchResultsList(List<SearchDTO> searchResultsList) {
		this.searchResultsList = searchResultsList;
	}
	

}
