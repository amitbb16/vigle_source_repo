package com.retro.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Likes;

@Repository
public interface LikesRepository extends CrudRepository<Likes, Integer> {
    
	List<Likes> findAllByLikedByUserId(Long uid);
    
    List<Likes> findAllBylikedToUserId(Long likedToUserId);
    
    List<Likes> findAllByLikedToServiceId(Long likedToServiceId);
    
    List<Likes> findAll();

    Likes findById(Long id);
    
    List<Likes> findByLikedByUserIdAndLikedToServiceId(Long likedByUserId, Long likedToServiceId);
    
    List<Likes> findByLikedByUserIdAndLikedToOrderId(Long likedByUserId, Long likedToOrderId);
    
    List<Likes> findByLikedByUserIdAndCommentedOnUserId(Long likedByUserId, Long commentedOnUserId);
    
    List<Likes> findByLikedByUserIdAndLikedToUserId(Long likedByUserId, Long likedToUserId);   
    
    @Query( "select count(o) from Likes o where o.likedToFeedsId=:feedId" )
    Long getFeedLikesCount(Long feedId);
    
    @Query( "select o.likedByUserId from Likes o where o.likedToFeedsId=:feedId" )
    List<Long> getFeedLikedBy(Long feedId);
}