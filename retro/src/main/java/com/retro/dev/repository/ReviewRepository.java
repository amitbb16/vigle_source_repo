package com.retro.dev.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    
	List<Review> findAllByUserid(Long uid);
    
    List<Review> findAllByServiceid(Long serviceid);
    
    List<Review> findAllByUseridAndServiceid(Long uid, long serviceid);
    
    List<Review> findAllByUseridAndChooseOrderId(Long uid, long chooseOrderId);
    
    List<Review> findAllByUseridAndServiceRequestId(Long uid, long serviceRequestId);
    
    List<Review> findAllByUseridAndDiscountId(Long uid, long discountId);
    
    Review findById(Long id);
    
}