package com.retro.dev.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

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

import com.retro.dev.models.ChooseOrder;
import com.retro.dev.security.services.ChooseOrderService;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/provider")
public class ChooseOrderController {

    @Autowired
    ChooseOrderService serviceRequestService;


    @PostMapping("/createProviderOrder")
    public ResponseEntity<?> createProviderOrder(Principal principal,final HttpServletRequest request,@RequestBody ChooseOrder chooseOrderRequest){
        return serviceRequestService.createProviderOrder(principal.getName(), chooseOrderRequest, request);
    }

    @GetMapping("/getProviderOrderDetails")
    public ResponseEntity<?> getProviderOrderDetails(Principal principal, @RequestParam(name = "id") Long id){
        return ResponseEntity.ok(serviceRequestService.getProviderOrderDetails(principal, id));
    }

    @GetMapping("/getCurrentProviderOrderList")
    public ResponseEntity<?> getCurrentProviderOrderList(Principal principal){
        return ResponseEntity.ok(serviceRequestService.getCurrentProviderOrderList(principal.getName()));
    }
    
    @GetMapping("/getAllProvidersOrderList")
    public ResponseEntity<?> getAllProvidersOrderList(Principal principal){
        return ResponseEntity.ok(serviceRequestService.getAllProvidersOrderList(principal.getName()));
    }
    
    @PutMapping("/updateProviderOrder")
    public ResponseEntity<?> updateProviderOrder(Principal principal,final HttpServletRequest request, @RequestBody ChooseOrder chooseOrderRequest){
        return serviceRequestService.updateProviderOrder(principal.getName(), chooseOrderRequest, request);
    }
    
    

}
