package com.retro.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.FeedMediaLink;

@Repository
public interface FeedMediaLinkRepository extends CrudRepository<FeedMediaLink, Long> {

    @Query( "select o from FeedMediaLink o where feedId=:feedId" )
    List<FeedMediaLink> findAllById(Long feedId);

}
