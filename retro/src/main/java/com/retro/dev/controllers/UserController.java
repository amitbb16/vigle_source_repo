package com.retro.dev.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.dtos.UserDto;
import com.retro.dev.models.User;
import com.retro.dev.payload.response.MessageResponse;
import com.retro.dev.payload.response.UserResponse;
import com.retro.dev.security.services.JwtUserDetailsService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {
    
	@Autowired
    private JwtUserDetailsService userDetailsService;


    @GetMapping("/vigleProfile")
    public ResponseEntity<?> getUserProfile(Principal principal){
        return ResponseEntity.ok(userDetailsService.loadUserProfile(principal.getName()));
    }

    @PostMapping("/vigleProfile")
    public ResponseEntity<?> editUserProfile(Principal principal, @ModelAttribute UserDto user) throws Exception {
       User res= userDetailsService.update(principal.getName(),user);
        if(res != null)
            return ResponseEntity.ok(new MessageResponse("update profile successfull"));

        else
            return ResponseEntity.badRequest().body(new MessageResponse("update profile update failed."));

    }
    
    @GetMapping(path = "downloadBackgroundImage")
    public ResponseEntity<?> downloadBackgroundImage(Principal principal) throws Exception {
    	Optional<User> userData = userDetailsService.loadUserProfileDataAndImages(principal.getName());
    	if(null==userData) {
    		return ResponseEntity.badRequest().body(new MessageResponse("user not found!"));
    	}
    	if(userData.get().getBackgroundImage().isEmpty()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Background Image not found!"));
    	}
    	File backGndImg = new File(System.getProperty("user.dir")+File.separator+ userData.get().getBackgroundImage());
    	InputStream targetStream = new FileInputStream(backGndImg);
    	InputStreamResource resource = new InputStreamResource(targetStream);

        long fileLength = backGndImg.length();
        return ResponseEntity.ok().contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        
    }
    
    @GetMapping(path = "downloadProfileImage")
    public ResponseEntity<?> downloadProfileImage(Principal principal) throws Exception {
    	Optional<User> userData = userDetailsService.loadUserProfileDataAndImages(principal.getName());
    	if(null==userData) {
    		return ResponseEntity.badRequest().body(new MessageResponse("user not found!"));
    	}
    	if(userData.get().getProfileImage().isEmpty()) {
    		return ResponseEntity.badRequest().body(new MessageResponse("Profile Image not found!"));
    	}
    	File profileImg = new File(System.getProperty("user.dir")+File.separator+ userData.get().getProfileImage());
    	InputStream targetStream = new FileInputStream(profileImg);
    	InputStreamResource resource = new InputStreamResource(targetStream);

        long fileLength = profileImg.length();
        return ResponseEntity.ok().contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        
    }
    
    @GetMapping(path = "/getUserAdditionalDetails")
    public UserResponse getUserAdditionalDetails(Principal principal, @RequestParam("id") final Long userId) throws Exception {
        return userDetailsService.getUserAdditionalDetails(principal, userId);
    }
    
    
}
