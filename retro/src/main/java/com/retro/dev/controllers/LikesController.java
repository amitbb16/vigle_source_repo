package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.LikesRequest;
import com.retro.dev.payload.response.LikesResponse;
import com.retro.dev.security.services.LikesServices;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/likes")
public class LikesController {

    @Autowired
    LikesServices commentsService;

    @PostMapping("/listAllLikes")
    public LikesResponse listAllLikes(Principal principal, @RequestBody LikesRequest listComments) {
        return commentsService.listAllLikes(principal, listComments);
    }
    
    @PostMapping("/listAllLikesBasedOnUser")
    public LikesResponse listAllLikesBasedOnUser(Principal principal, @RequestBody LikesRequest LikesRequest) {
        return commentsService.listAllLikesBasedOnUser(principal, LikesRequest);
    }
    
    @PostMapping("/listAllLikesBasedOnService")
    public LikesResponse listAllLikesBasedOnService(Principal principal, @RequestBody LikesRequest LikesRequest) {
        return commentsService.listAllLikesBasedOnService(principal, LikesRequest);
    }
    
    @PostMapping("/listAllLikesBasedOnCommentedOnUserId")
    public LikesResponse listAllLikesBasedOnCommentedOnUserId(Principal principal, @RequestBody LikesRequest LikesRequest) {
        return commentsService.listAllLikesBasedOnCommentedOnUserId(principal, LikesRequest);
    }
    
    @PostMapping("/listAllLikesBasedOnlikedToUserId")
    public LikesResponse listAllLikesBasedOnlikedToUserId(Principal principal, @RequestBody LikesRequest LikesRequest) {
        return commentsService.listAllLikesBasedOnlikedToUserId(principal, LikesRequest);
    }
    
    @PostMapping("/getLikesDetail")
    public LikesResponse getLikesDetail(Principal principal, @RequestBody LikesRequest LikesRequest) {
        return commentsService.getLikesDetail(principal, LikesRequest);
    }

    @PostMapping("/addLike")
    public LikesResponse addLike(Principal principal, @RequestBody LikesRequest LikesRequest) {
        return commentsService.addLike(principal.getName(), LikesRequest);
    }
    
    @PostMapping("/updateLike")
    public LikesResponse updateLike(Principal principal, @RequestBody LikesRequest LikesRequest) {
        return commentsService.updateLike(principal.getName(), LikesRequest);
    }

    @PostMapping("/removeLike")
    public LikesResponse removeLike(Principal principal, @RequestBody LikesRequest LikesRequest) {
        return commentsService.removeLike(principal.getName(), LikesRequest);
    }

}
