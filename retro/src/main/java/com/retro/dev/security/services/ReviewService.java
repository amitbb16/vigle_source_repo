package com.retro.dev.security.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.retro.dev.dtos.ReviewDTO;
import com.retro.dev.models.Review;
import com.retro.dev.models.User;
import com.retro.dev.payload.response.ReviewResponse;
import com.retro.dev.payload.response.UserResponse;
import com.retro.dev.repository.ReviewRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.PublicConstants;

@Service
public class ReviewService {
    
	@Autowired
    ReviewRepository reviewRepository;
   
	@Autowired
    UserRepository userRepository;

    
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    
    public ResponseEntity<?> save(String username, Review review){
        Optional<User> user = userRepository.findByUsername(username);
        Review newReview = new Review(user.get().getId(), review.getServiceid(),  review.getChooseOrderId(), 
        		review.getServiceRequestId(), review.getDiscountId(), review.getComment(), review.getRating(),
        		review.getCreatedDate(), review.getLastUpdatedDate());
        Review reviewres = reviewRepository.save(newReview);
        if(reviewres != null)
            return ResponseEntity.ok("review saved");
        else
            return ResponseEntity.badRequest().body("failed to save the review");
    }

    public ResponseEntity<?> update(String username, Review pReviwe){
        Review review = reviewRepository.findById(pReviwe.getId());
        review.setUserid(pReviwe.getUserid());
        review.setServiceid(pReviwe.getServiceid());
        review.setComment(pReviwe.getComment());
        review.setRating(pReviwe.getRating());
        review.setLastUpdatedDate(pReviwe.getLastUpdatedDate());
        Review reviewres = reviewRepository.save(review);
        if(reviewres != null)
            return ResponseEntity.ok("review updated");
        else
            return ResponseEntity.badRequest().body("failed to update the review");
    }


    public List<Review> listReview(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return reviewRepository.findAllByUserid(user.get().getId());
    }

    public ReviewResponse listReviewByServiceid(String username, Long serviceid){
        Optional<User> user = userRepository.findByUsername(username);
        ReviewResponse response = new ReviewResponse();
        if(user.get().getId() != null)
        {
        	List<Review> reviewersList = reviewRepository.findAllByServiceid(serviceid);
        	List<Long> reviwerIds = new ArrayList<Long>();
        	for(Review eachReviewer : reviewersList) {
        		reviwerIds.add(eachReviewer.getId());
        	}
        	List<User> allReviewsList = userRepository.findAllById(reviwerIds);
        	reviwerIds = null;
        	List<ReviewDTO> reviewersDTOList = new ArrayList<ReviewDTO>();
        	for(Review eachReviewer : reviewersList) {
        		for(User eachUser : allReviewsList) {
        			if(null!=eachReviewer&&null!=eachUser&&eachReviewer.getUserid()==eachUser.getId()) {
        				ReviewDTO eachReviewerDTO = new ReviewDTO();
        				BeanUtils.copyProperties(eachReviewer, eachReviewerDTO);
        				setUserDetailsInReviewDTO(eachUser, eachReviewerDTO);
        				reviewersDTOList.add(eachReviewerDTO);
        			}
        		}
        	}
        	response = generateFinalResponse(reviewersDTOList,"reviewers fetched successfully.");
        }
        return response;
    }

	public ReviewResponse listAllUserReviews(String username) {
		ReviewResponse response = new ReviewResponse();
		Optional<User> user = userRepository.findByUsername(username);
		List<ReviewDTO> userReviewDTOList = new ArrayList<ReviewDTO>();
		if(null==user || !user.isPresent()) {
			return generateFinalResponse(userReviewDTOList, "User not found!!");
		}
		Iterable<Review> userReviewsList = reviewRepository.findAll();
		List<Review> alluserReviewsList = StreamSupport.stream(userReviewsList.spliterator(), false).collect(Collectors.toList());
		for(Review eachReview : alluserReviewsList) {
			ReviewDTO eachReviewDTO = new ReviewDTO();
			BeanUtils.copyProperties(eachReview, eachReviewDTO);
			setUserDetailsInReviewDTO(user.get(), eachReviewDTO);
			userReviewDTOList.add(eachReviewDTO);
		}
		response = generateFinalResponse(userReviewDTOList, "User Reviews fetched successfully.");
		return response;
	}

	private void setUserDetailsInReviewDTO(User eachUser, ReviewDTO eachReviewerDTO) {
    	eachReviewerDTO.setUserName(eachUser.getUsername());
    	eachReviewerDTO.setName(eachUser.getName());
    	if(!StringUtils.isEmpty(eachUser.getProfileImage())) {
			File userProfileImg = new File(eachUser.getProfileImage());
			String stringEncodedProfileImg = "";
			if(userProfileImg.exists()) {
				try {
					byte[] bgImgfileContent = FileUtils.readFileToByteArray(userProfileImg);
					stringEncodedProfileImg = Base64.getEncoder().encodeToString(bgImgfileContent);
				} catch (IOException e) {
					logger.error("error generating User Profile in setUserDetailsInReviewDTO");
					e.printStackTrace();
				}
			}
			eachReviewerDTO.setProfileImage(stringEncodedProfileImg);
		}
		
	}
	 
    private ReviewResponse generateFinalResponse(List<ReviewDTO> reviewersDTOList, String respDesc) {
    	ReviewResponse response = new ReviewResponse();
    	response.setResponseStatus(PublicConstants.SUCCESS);
    	response.setResponseDesc(respDesc);
    	response.setReviewsList(reviewersDTOList);
		return response;
	}

    public List<Review> listReviewByServiceidAndUserid(String username, Long serviceid){
        Optional<User> user = userRepository.findByUsername(username);
            return reviewRepository.findAllByUseridAndServiceid(user.get().getId(), serviceid);
    }

	public UserResponse getUserAvgRatings(User user, UserResponse response) {
		float totalRating = 0;
		float avgRating = 0;
		List<Review> userReviews = listReview(user.getUsername());
		if(null!=userReviews) {
			for(Review eachReview : userReviews) {
				totalRating = totalRating+eachReview.getRating();
			}
			avgRating = totalRating/userReviews.size();
		}
		response.setRating(avgRating);
		return response;
	}

}
