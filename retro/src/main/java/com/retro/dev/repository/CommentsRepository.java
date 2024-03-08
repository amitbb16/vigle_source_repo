package com.retro.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Comments;

@Repository
public interface CommentsRepository extends CrudRepository<Comments, Integer> {
    
	List<Comments> findAllByUserId(Long uid);
    
    List<Comments> findAllBylikedToUserId(Long likedToUserId);
    
    List<Comments> findAllByCommentedOnUserId(Long commentedOnUserId);
    
    List<Comments> findAllByServiceId(Long serviceId);
    
    @Query( "select o from Comments o where o.feedId=:feedId and o.isCommented=:isCommented" )
    List<Comments> findAllByFeedId(Long feedId, String isCommented);

    List<Comments> findAll();

    Comments findById(Long id);
    
    List<Comments> findByUserIdAndServiceId(Long userId, Long serviceId);
    
    List<Comments> findByUserIdAndOrderId(Long userId, Long orderId);
    
    List<Comments> findByUserIdAndCommentedOnUserId(Long userId, Long commentedOnUserId);
    
    List<Comments> findByUserIdAndLikedToUserId(Long userId, Long likedToUserId);
    
    @Query( "select o from Comments o where feedId=:feedId" )
    List<Comments> getFeedComments(Long feedId);

}