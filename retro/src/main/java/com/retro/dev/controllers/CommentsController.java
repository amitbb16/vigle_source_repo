package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.CommentsRequest;
import com.retro.dev.payload.response.CommentsResponse;
import com.retro.dev.security.services.CommentServices;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    CommentServices commentsService;

    @PostMapping("/listAllComments")
    public CommentsResponse listAllComments(Principal principal, @RequestBody CommentsRequest listComments) {
        return commentsService.listAllComments(principal, listComments);
    }
    
    @PostMapping("/listAllCommentsBasedOnUser")
    public CommentsResponse listAllCommentsBasedOnUser(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.listAllCommentsBasedOnUser(principal, commentsRequest);
    }
    
    @PostMapping("/listAllCommentsBasedOnService")
    public CommentsResponse listAllCommentsBasedOnService(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.listAllCommentsBasedOnService(principal, commentsRequest);
    }
    
    @PostMapping("/listAllCommentsBasedOnOnFeedId")
    public CommentsResponse listAllCommentsBasedOnOnFeedId(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.listAllCommentsBasedOnFeed(principal, commentsRequest);
    }
    
    @PostMapping("/listAllCommentsBasedOnCommentedOnUserId")
    public CommentsResponse listAllCommentsBasedOnCommentedOnUserId(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.listAllCommentsBasedOnCommentedOnUserId(principal, commentsRequest);
    }
    
    @PostMapping("/listAllCommentsBasedOnlikedToUserId")
    public CommentsResponse listAllCommentsBasedOnlikedToUserId(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.listAllCommentsBasedOnlikedToUserId(principal, commentsRequest);
    }
    
    @PostMapping("/getCommentsDetail")
    public CommentsResponse getCommentsDetail(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.getCommentsDetail(principal, commentsRequest);
    }

    @PostMapping("/addComment")
    public CommentsResponse addComment(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.addComment(principal.getName(), commentsRequest, "add");
    }
    
    @PostMapping("/updateComment")
    public CommentsResponse updateComment(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.updateComment(principal.getName(), commentsRequest);
    }

    @PostMapping("/removeComment")
    public CommentsResponse removeComment(Principal principal, @RequestBody CommentsRequest commentsRequest) {
        return commentsService.removeComment(principal.getName(), commentsRequest);
    }

}
