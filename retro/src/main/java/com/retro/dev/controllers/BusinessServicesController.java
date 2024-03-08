package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.BusinessServicesRequest;
import com.retro.dev.payload.response.BusinessServicesResponse;
import com.retro.dev.security.services.BusinessService;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/business")
public class BusinessServicesController {

    @Autowired
    BusinessService businessService;

    @PostMapping("/service/all")
    public ResponseEntity<?> getBusinessServicesList(Principal principal, @RequestBody BusinessServicesRequest listBservices) {
        return ResponseEntity.ok(businessService.listBusinessServices(principal, listBservices));
    }

    @PostMapping("/serviceDetail")
    public BusinessServicesResponse getBusinessServiceDetail(Principal principal, @RequestBody BusinessServicesRequest listBservices) {
        return businessService.getBusinessServiceDetail(principal, listBservices);
    }

    @PostMapping("/createService")
    public BusinessServicesResponse createService(Principal principal, @RequestBody BusinessServicesRequest createBusinessService) {
        return businessService.save(principal.getName(), createBusinessService);
    }

    @PutMapping("/updateService")
    public BusinessServicesResponse updateService(Principal principal, @RequestBody BusinessServicesRequest updateBusinessService) {
        return businessService.update(principal.getName(), updateBusinessService);
    }
    
}
