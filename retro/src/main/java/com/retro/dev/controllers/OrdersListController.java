package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.UserOrdersListRequest;
import com.retro.dev.payload.response.UserOrdersListResponse;
import com.retro.dev.security.services.OrdersListService;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class OrdersListController {

    
    @Autowired
    OrdersListService orderListService;
	
    @PostMapping("/getUserOrdersList")
    public UserOrdersListResponse getUserOrdersList(Principal principal, @RequestBody UserOrdersListRequest orderListRequest) {
        return orderListService.getUserOrdersList(principal, orderListRequest);
    }
    
    @PostMapping("/getUserOrderRequestsList")
    public UserOrdersListResponse getUserOrderRequestsList(Principal principal, @RequestBody UserOrdersListRequest orderListRequest) {
        return orderListService.getUserOrderRequestsList(principal, orderListRequest);
    }
    
    @PostMapping("/getUserDiscountList")
    public UserOrdersListResponse getUserDiscountList(Principal principal, @RequestBody UserOrdersListRequest orderListRequest) {
        return orderListService.getUserDiscountList(principal, orderListRequest);
    }
    
    
}
