package com.retro.dev.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Deal;

import java.util.List;

@Repository
public interface DealsRepository extends CrudRepository<Deal, Integer> {
    List<Deal> findAllByUid(Long uid);

    Deal findById(Long id);
}