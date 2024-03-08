package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.CategoryRequest;
import com.retro.dev.payload.response.CategoryResponse;
import com.retro.dev.security.services.CategoryService;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;



    @PostMapping("/createCategory")
    public CategoryResponse createCategory(Principal principal, @RequestBody CategoryRequest categoryReq) {
        return categoryService.createCategory(principal, categoryReq);
    }
    
    @GetMapping("/getAllCategories")
    public CategoryResponse getAllCategories(Principal principal) {
        return categoryService.listAllCategory(principal);
    }

    @GetMapping("/getCategoryDetail")
    public CategoryResponse getCategoryDetail(Principal principal, @RequestParam(name = "categoryId") Long categoryId) {
        return categoryService.getCategoryDetail(principal, categoryId);
    }
    
    
    
    
    @PostMapping("/createSubCategory")
    public CategoryResponse createSubCategory(Principal principal, @RequestBody CategoryRequest categoryReq) {
        return categoryService.createSubCategory(principal, categoryReq);
    }

    @GetMapping("/getAllSubCategories")
    public CategoryResponse getAllSubCategories(Principal principal) {
        return categoryService.listAllSubCategory(principal);
    }
    

    @GetMapping("/getAllSubCategoriesBasedOnCategoryId")
    public CategoryResponse getAllSubCategoriesBasedOnCategoryId(Principal principal, @RequestParam(name = "categoryId") Long categoryId) {
        return categoryService.getAllSubCategoriesBasedOnCategoryId(principal, categoryId);
    }
    

    @GetMapping("/getSubCategoryDetail")
    public CategoryResponse getSubCategoryDetail(Principal principal, @RequestParam(name = "subcategoryId") Long subcategoryId) {
        return categoryService.getSubCategoryDetail(principal, subcategoryId);
    }
    
    
    
    @PostMapping("/saveUserCategory")
    public CategoryResponse createUserCategory(Principal principal, @RequestBody CategoryRequest categoryReq) {
        return categoryService.createUserCategory(principal, categoryReq);
    }

    @GetMapping("/listCategoryByUserId")
    public CategoryResponse getUserCategories(Principal principal, @RequestParam(name = "userId") Long userId) {
        return categoryService.listCategoryByUserId(principal, userId);
    }
    
    @GetMapping("/getUserCategoryDetail")
    public CategoryResponse getUserCategoryDetail(Principal principal, @RequestParam(name = "userId") Long userId, @RequestParam(name = "userCategoryId") Long userCategoryId) {
        return categoryService.getUserCategoryDetail(principal, userId, userCategoryId);
    }
    
    @PutMapping("/updateUserCategory")
    public CategoryResponse updateUserCategory(Principal principal, @RequestBody CategoryRequest categoryReq) {
        return categoryService.updateUserCategory(principal, categoryReq);
    }
    
}
