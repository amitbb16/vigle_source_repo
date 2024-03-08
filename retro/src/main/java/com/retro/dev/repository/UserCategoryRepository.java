package com.retro.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.UserCategory;

@Repository
public interface UserCategoryRepository extends CrudRepository<UserCategory, Integer> {
    List<UserCategory> findAllByUserId(Long uid);

    List<UserCategory> findAll();

    UserCategory findByUserCategoryId(Long id);
}