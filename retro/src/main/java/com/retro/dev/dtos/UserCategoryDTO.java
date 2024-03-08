package com.retro.dev.dtos;

import com.retro.dev.models.Category;
import com.retro.dev.models.SubCategory;

public class UserCategoryDTO {
    
	
    private Category category;
    private SubCategory subcategory;
	
    
    
    public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public SubCategory getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(SubCategory subcategory) {
		this.subcategory = subcategory;
	}
    
	


    
}
