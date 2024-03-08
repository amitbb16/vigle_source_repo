package com.retro.dev.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.retro.dev.dtos.BusinessServicesDTO;
import com.retro.dev.payload.request.BusinessServicesRequest;
import com.retro.dev.payload.request.ServiceRequestRequest;
import com.retro.dev.payload.response.BusinessServicesResponse;
import com.retro.dev.security.services.ServiceRequestService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ServiceRequestController {

    @Autowired
    ServiceRequestService serviceRequestService;

    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestController.class);
    
    @GetMapping("/serviceRequest")
    public ResponseEntity<?> getServiceRequests(Principal principal){
        return ResponseEntity.ok(serviceRequestService.listServiceRequests(principal.getName()));
    }

    @PostMapping("/serviceRequest")
    public ResponseEntity<?> createServiceRequest(Principal principal,  @RequestParam(name = "servicesRequestMedias") List<MultipartFile> servicesRequestMedias,
    		 @RequestParam(name = "serviceRequestString") String serviceRequestString){
    	ServiceRequestRequest serviceRequest = new ServiceRequestRequest();
    	if(StringUtils.isNotEmpty(serviceRequestString)) {
        	ObjectMapper mapper = new ObjectMapper();
        	mapper.registerModule(new JavaTimeModule());
        	try {
				serviceRequest = (ServiceRequestRequest) mapper.readValue(serviceRequestString, serviceRequest.getClass());
			} catch (IOException e) {
				logger.error("failed to read value from serviceRequestString ");;
			}
        }
    	if(null!=serviceRequest && null!=serviceRequest.getServiceReq() && null!=servicesRequestMedias) {
        	serviceRequest.getServiceReq().setServicesRequestMedias(servicesRequestMedias);
        }
    	return serviceRequestService.save(principal.getName(), serviceRequest);
    }

    @PutMapping("/serviceRequest")
    public ResponseEntity<?> updateServiceRequest(Principal principal, @RequestBody ServiceRequestRequest serviceRequest){
        return ResponseEntity.ok(serviceRequestService.update(principal.getName(), serviceRequest));
    }
    
    

    @PostMapping("/user/createOrder")
    public BusinessServicesResponse createOrder(Principal principal, @RequestBody BusinessServicesRequest createBusinessService) {
        return serviceRequestService.createOrder(principal.getName(), createBusinessService);
    }

    @PostMapping("/user/orderDetail")
    public BusinessServicesResponse getUserOrderDetail(Principal principal, @RequestBody BusinessServicesRequest listBservices) {
        return serviceRequestService.getUserOrderDetail(principal, listBservices);
    }
    
    @PostMapping("/user/allOrdersList")
    public List<BusinessServicesDTO> getAllOrdersList(Principal principal, @RequestBody BusinessServicesRequest listBservices) {
        return serviceRequestService.getAllOrdersList(principal, listBservices);
    }

}
