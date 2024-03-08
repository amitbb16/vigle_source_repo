package com.retro.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Feed;

@Repository
public interface FeedRepository extends CrudRepository<Feed, Long> {
    
    Optional<Feed> findById(Long id);

    @Query( "select o from Feed o where userId=:userId order by lastUpdatedDate desc" )
    List<Feed> findAllByUserId(Long userId);

}
