package com.retro.dev.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Notification;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    
	List<Notification> findAllByUserid(Long uid);

	Notification findById(Long id);
}