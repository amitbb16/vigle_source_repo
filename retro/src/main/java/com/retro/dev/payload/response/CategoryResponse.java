package com.retro.dev.payload.response;

import java.util.List;

import com.retro.dev.models.Category;
import com.retro.dev.models.SubCategory;
import com.retro.dev.models.UserCategory;

public class CategoryResponse extends PaginationGenericResponse{

    
	
	private String category;
    private Long categoryId;
    private Long subCategoryId;
    private String subcategory;
    private Long userCategoryId;
    private Category categoryObj;
    private SubCategory subcategoryObj;
    private UserCategory userCategoryObj;
    private List<UserCategory> userCategoryList;
    private List<Category> categoryList;
    private List<SubCategory> subCategoryList;
    
    
    public CategoryResponse() {
    	
    }

    public CategoryResponse( String category, String subcategory) {
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public Long getUserCategoryId() {
		return userCategoryId;
	}

	public void setUserCategoryId(Long userCategoryId) {
		this.userCategoryId = userCategoryId;
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

	public List<UserCategory> getUserCategoryList() {
		return userCategoryList;
	}

	public void setUserCategoryList(List<UserCategory> userCategoryList) {
		this.userCategoryList = userCategoryList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<SubCategory> getSubCategoryList() {
		return subCategoryList;
	}

	public void setSubCategoryList(List<SubCategory> subCategoryList) {
		this.subCategoryList = subCategoryList;
	}

	public UserCategory getUserCategoryObj() {
		return userCategoryObj;
	}

	public void setUserCategoryObj(UserCategory userCategoryObj) {
		this.userCategoryObj = userCategoryObj;
	}

    
    
}
