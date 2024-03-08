package com.retro.dev.controllers;

import com.retro.dev.models.Review;
import com.retro.dev.payload.response.ReviewResponse;
import com.retro.dev.security.services.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/review")
    public ResponseEntity<?> getReviews(Principal principal){
        return ResponseEntity.ok(reviewService.listReview(principal.getName()));
    }

    @GetMapping("/review/services")
    public ReviewResponse listReviewsByServiceId(Principal principal, @RequestParam(name = "serviceId") Long serviceid){
        return reviewService.listReviewByServiceid(principal.getName(), serviceid);
    }

    @GetMapping("/review/servce/user")
    public ResponseEntity<?> listReviewsByServiceIdAndUserid(Principal principal, @RequestParam(name = "serviceId") Long serviceId){
        return ResponseEntity.ok(reviewService.listReviewByServiceidAndUserid(principal.getName(), serviceId));
    }

    @PostMapping("/review")
    public ResponseEntity<?> addReview(Principal principal,@RequestBody Review review){
        return reviewService.save(principal.getName(), review);
    }

    @PutMapping("/review")
    public ResponseEntity<?> updateReview(Principal principal, @RequestBody Review review){
        return reviewService.update(principal.getName(), review);
    }
    
    @GetMapping("/review/listAllUserReviews")
    public ReviewResponse listAllUserReviews(Principal principal){
        return reviewService.listAllUserReviews(principal.getName());
    }

}
