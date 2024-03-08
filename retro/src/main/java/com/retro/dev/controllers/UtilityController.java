package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.CountryRequest;
import com.retro.dev.payload.response.CountryResponse;
import com.retro.dev.security.services.CountryService;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/utils")
public class UtilityController {

    
    @Autowired
    CountryService countryService;
	
    @GetMapping("/getCountriesList")
    public CountryResponse getCountriesList(Principal principal) {
        return countryService.getCountriesList(principal);
    }
    
    @PostMapping("/getCountryDetails")
    public CountryResponse getCountryDetails(Principal principal, @RequestBody CountryRequest countryRequest) {
        return countryService.getCountryDetails(principal, countryRequest);
    }
    
    
}
