package com.retro.dev.controllers;

import com.retro.dev.models.Deal;
import com.retro.dev.security.services.UserDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/deal")
public class DealController {

    @Autowired
    UserDealService userDealService;

    @GetMapping("/userDeal")
    public ResponseEntity<?> getDeals(Principal principal){
        return ResponseEntity.ok(userDealService.listDeals(principal.getName()));
    }

    @PostMapping("/userDeal")
    public ResponseEntity<?> createDeal(Principal principal,@RequestBody Deal userDto){
        return userDealService.save(principal.getName(), userDto);
    }

    @PutMapping("/userDeal")
    public ResponseEntity<?> updateDeal(Principal principal, @RequestBody Deal userDto){
        return userDealService.update(principal.getName(), userDto);
    }
}
