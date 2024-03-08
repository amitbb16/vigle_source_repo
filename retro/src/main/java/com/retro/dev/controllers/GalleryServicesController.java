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
import com.retro.dev.security.services.GalleryService;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/gallery")
public class GalleryServicesController {

    @Autowired
    GalleryService galleryService;

    @PostMapping("/getMediaLinks")
    public ResponseEntity<?> getBusinessServicesList(Principal principal, @RequestBody BusinessServicesRequest listBservices) {
        return ResponseEntity.ok(galleryService.getMediaLinksForGallery(principal, listBservices));
    }
}
