package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.dtos.CountryDTO;

public class CountryResponse extends PaginationGenericResponse{

    

	//private attributes
    ////////////////////
    private List<CountryDTO> countrysList;
    private CountryDTO countryDetails;

    //public attributes
    ////////////////////
	public List<CountryDTO> getCountrysList() {
		return countrysList;
	}
	public void setCountrysList(List<CountryDTO> countrysList) {
		this.countrysList = countrysList;
	}
	public CountryDTO getCountryDetails() {
		return countryDetails;
	}
	public void setCountryDetails(CountryDTO countryDetails) {
		this.countryDetails = countryDetails;
	}
    
}
