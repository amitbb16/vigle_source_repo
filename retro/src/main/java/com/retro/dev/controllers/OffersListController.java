package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.BusinessOffersListRequest;
import com.retro.dev.payload.response.BusinessOffersListResponse;
import com.retro.dev.security.services.OffersListService;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/businessOffers")
public class OffersListController {

    
    @Autowired
    OffersListService offerListService;
	
    @PostMapping("/getBusinessOffersList")
    public BusinessOffersListResponse getBusinessOffersList(Principal principal, @RequestBody BusinessOffersListRequest offerListRequest) {
        return offerListService.getBusinessOffersList(principal, offerListRequest);
    }
    
    @PostMapping("/getBusinessDiscountList")
    public BusinessOffersListResponse getBusinessDiscountList(Principal principal, @RequestBody BusinessOffersListRequest offerListRequest) {
        return offerListService.getBusinessDiscountList(principal, offerListRequest);
    }
    
    
}
