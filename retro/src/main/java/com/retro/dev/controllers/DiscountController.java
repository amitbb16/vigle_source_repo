package com.retro.dev.controllers;

import com.retro.dev.models.Discount;
import com.retro.dev.security.services.DiscountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @GetMapping("/discount")
    public ResponseEntity<?> listDiscount(Principal principal){
        return ResponseEntity.ok(discountService.listDiscount(principal.getName()));
    }

    @PostMapping("/discount")
    public ResponseEntity<?> createDiscount(Principal principal,@RequestBody Discount discount){
        return discountService.save(principal.getName(), discount);
    }

    @PutMapping("/discount")
    public ResponseEntity<?> updateDiscount(Principal principal, @RequestBody Discount discount){
        return discountService.update(principal.getName(), discount);
    }
}
