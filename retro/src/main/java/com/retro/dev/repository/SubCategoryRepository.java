package com.retro.dev.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Category;
import com.retro.dev.models.SubCategory;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, Integer> {

    List<SubCategory> findAll();

    SubCategory findBySubcategoryId(Long id);
    
    SubCategory findBySubcategory(String subcategory);
    
    List<SubCategory> findByCategory(Category category);
}