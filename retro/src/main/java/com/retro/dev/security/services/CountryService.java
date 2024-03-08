package com.retro.dev.security.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.CountryDTO;
import com.retro.dev.models.Country;
import com.retro.dev.payload.request.CountryRequest;
import com.retro.dev.payload.response.CountryResponse;
import com.retro.dev.repository.CountryRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.DevUtils;
import com.retro.dev.util.PublicConstants;



@Service
public class CountryService {
    
	@Autowired
	CountryRepository countryServiceDao;
    
	@Autowired
    UserRepository userRepository;
    
    @Autowired
    DevUtils utils;
    

    
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    


	public CountryResponse getCountriesList(Principal principal) {
		CountryResponse response = new CountryResponse();
		try {
			List<Country> countriesList = countryServiceDao.findAll();
			if (null!= countriesList) {
				List<CountryDTO> countriesDTOList = new ArrayList<CountryDTO>();
				for(Country eachCountry : countriesList) {
					CountryDTO eachCountryDTO = new CountryDTO();
					BeanUtils.copyProperties(eachCountry, eachCountryDTO);
					countriesDTOList.add(eachCountryDTO);
				}
				response.setCountrysList(countriesDTOList);
				response.setTotalCount(countriesDTOList.size());
				response = generateCountryListSuccessResponse("Countries List fetched successfully.", response);
			} else {
				response = generateCountryListSuccessResponse("Countries List could not be fetched. Please check the country table.", response);
			}
		} catch (BeansException e) {
			response = generateCountryListFailResponse("Error fetching countries List. Please contact admin.", response);
			logger.error(e.getMessage());
		}
		return response;
	}



	public CountryResponse getCountryDetails(Principal principal, CountryRequest countryRequest) {
		CountryResponse response = new CountryResponse();
		if(null!=countryRequest && null!=countryRequest.getCountry()) {
			Optional<Country> countryDetails = countryServiceDao.findById(countryRequest.getCountry().getId());
			if(null==countryDetails || !countryDetails.isPresent()) {
				return generateCountryListSuccessResponse("Country details could not be fetched. Please check the country table.", response);
			}
			CountryDTO countryDetailsDTO = new CountryDTO();
			BeanUtils.copyProperties(countryDetails.get(), countryDetailsDTO);
			response.setCountryDetails(countryDetailsDTO);
			response = generateCountryListSuccessResponse("Country details fetched successfully.", response);
		}
		return response;
	}
	   

	public CountryResponse generateCountryListSuccessResponse(String msg, CountryResponse response) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}

	public CountryResponse generateCountryListFailResponse(String msg, CountryResponse response) {
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		return response;
	}


}
