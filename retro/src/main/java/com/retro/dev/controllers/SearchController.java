package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.SearchRequest;
import com.retro.dev.payload.response.SearchResponse;
import com.retro.dev.security.services.SearchService;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mapSearch")
public class SearchController {

    @Autowired
    SearchService searchService;


    @PostMapping("/searchServicesAndOffers")
    public SearchResponse getBusinessServicesList(Principal principal, @RequestBody SearchRequest searchRequest) {
        return searchService.searchServicesAndOffers(principal, searchRequest);
    }
    
}
