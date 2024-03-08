package com.retro.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Favourites;

@Repository
public interface FavouritesRepository extends CrudRepository<Favourites, Integer> {
    List<Favourites> findAllByUserId(Long uid);
    
    List<Favourites> findAllByServiceId(Long serviceId);
    
    List<Favourites> findAllByFeedId(Long feedId);

    List<Favourites> findAll();

    Favourites findById(Long id);
    
    Favourites findByUserIdAndServiceId(Long userId, Long serviceId);
    
    Favourites findByUserIdAndOrderId(Long userId, Long orderId);
    
    Favourites findByUserIdAndFeedId(Long userId, Long feedId);
}