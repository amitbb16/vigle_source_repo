package com.retro.dev.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.retro.dev.models.Category;
import com.retro.dev.models.SubCategory;

public class CategoryDTO {
    
	
    private Long id;
    private String category;
    private Category categoryObj;
    private String categoryType;
    private Long categoryId;
    private Long userId;
    private Long subcategoryId;
    private Long userCategoryId;
    private String subcategory;
    private SubCategory subcategoryObj;
    private List<UserCategoryDTO> userCategoryList;
    
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedDate;


	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(Long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public Category getCategoryObj() {
		return categoryObj;
	}

	public void setCategoryObj(Category categoryObj) {
		this.categoryObj = categoryObj;
	}

	public SubCategory getSubcategoryObj() {
		return subcategoryObj;
	}

	public void setSubcategoryObj(SubCategory subcategoryObj) {
		this.subcategoryObj = subcategoryObj;
	}

	public Long getUserCategoryId() {
		return userCategoryId;
	}

	public void setUserCategoryId(Long userCategoryId) {
		this.userCategoryId = userCategoryId;
	}

	public List<UserCategoryDTO> getUserCategoryList() {
		return userCategoryList;
	}

	public void setUserCategoryList(List<UserCategoryDTO> userCategoryList) {
		this.userCategoryList = userCategoryList;
	}

    
}
