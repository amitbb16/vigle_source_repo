package com.retro.dev.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.EStatus;
import com.retro.dev.models.ServiceRequest;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServiceRequestRepository extends CrudRepository<ServiceRequest, Integer> {
    List<ServiceRequest> findAllByUserid(Long userid);

    ServiceRequest findById(Long id);
    
    List<ServiceRequest> findAllByUseridAndStatus(Long userid, EStatus status, Pageable pageable);
    
    List<ServiceRequest> findAllByServiceId(Long serviceId);
    
    List<ServiceRequest> findAllByCompletiondateAndStatus(LocalDateTime completiondate, EStatus status);
}