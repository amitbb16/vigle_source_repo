package com.retro.dev.payload.request;

import com.retro.dev.dtos.CountryDTO;


public class CountryRequest extends PaginationGenericRequest{
    
	//private attribs
	//////////////////
	private CountryDTO country;


	//public attribs
	//////////////////

	public CountryDTO getCountry() {
		return country;
	}

	public void setCountry(CountryDTO country) {
		this.country = country;
	}

    
}
