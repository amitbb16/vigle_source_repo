package com.retro.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    List<Category> findAll();

    Category findByCategoryId(Long id);
    
    Category findByCategory(String category);
    
}