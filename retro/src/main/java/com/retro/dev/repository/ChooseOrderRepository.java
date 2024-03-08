package com.retro.dev.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.ChooseOrder;
import com.retro.dev.models.EStatus;


/**
 * @author KumarSourav
 *
 *This Repo is actually used 
 *to store Business(Offers)
 *data i.e when a service provider 
 *accepts a User's Order
 */

@Repository
public interface ChooseOrderRepository extends CrudRepository<ChooseOrder, Integer> {
    
	List<ChooseOrder> findAllByUserid(Long userid);

    ChooseOrder findById(Long id);
    
    List<ChooseOrder> findAllByUseridAndStatus(Long userid, EStatus status, Pageable pageable);
    
    List<ChooseOrder> findAllByServiceId(Long serviceId);
    
    List<ChooseOrder> findAllByCompletiondateAndStatus(LocalDateTime completiondate, EStatus status);
    
}