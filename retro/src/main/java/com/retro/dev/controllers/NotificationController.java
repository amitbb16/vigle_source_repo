package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.NotificationRequest;
import com.retro.dev.payload.response.NotificationResponse;
import com.retro.dev.security.services.NotificationService;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/notification")
public class NotificationController {

    
    @Autowired
    NotificationService notificationService;
	
    @GetMapping("/getUserNotificationsList")
    public NotificationResponse getUserNotificationsList(Principal principal) {
        return notificationService.getUserNotificationsList(principal);
    }
    
    @GetMapping("/getNotificationDetails")
    public NotificationResponse getNotificationDetails(Principal principal, @RequestParam(name = "notificationId") Long notificationId) {
        return notificationService.getNotificationDetails(principal, notificationId);
    }
    
    @PostMapping("/createUserNotification")
    public NotificationResponse createUserNotification(Principal principal, @RequestBody NotificationRequest notificationRequest) {
        return notificationService.createUserNotification(principal.getName(), notificationRequest);
    }
    
    @PostMapping("/updateUserNotification")
    public NotificationResponse updateUserNotification(Principal principal, @RequestBody NotificationRequest notificationRequest) {
        return notificationService.updateUserNotification(principal, notificationRequest);
    }
    
    @DeleteMapping("/deleteNotification")
    public NotificationResponse deleteNotification(Principal principal, @RequestParam(name = "notificationId") Long notificationId) {
        return notificationService.deleteNotification(principal, notificationId);
    }
    
}
