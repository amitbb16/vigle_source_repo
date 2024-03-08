package com.retro.dev.payload.request;

import java.util.List;

import com.retro.dev.dtos.SearchDTO;


public class SearchRequest extends PaginationGenericRequest{
    
	//private attribs
	//////////////////
	private SearchDTO searchDTO;
	private List<String> searchFilterTypes;
    

	//public attribs
	//////////////////

	public SearchDTO getSearchDTO() {
		return searchDTO;
	}


	public void setSearchDTO(SearchDTO searchDTO) {
		this.searchDTO = searchDTO;
	}


	public List<String> getSearchFilterTypes() {
		return searchFilterTypes;
	}


	public void setSearchFilterTypes(List<String> searchFilterTypes) {
		this.searchFilterTypes = searchFilterTypes;
	}

    
}
