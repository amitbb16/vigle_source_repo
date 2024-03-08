package com.retro.dev.payload.request;

import com.retro.dev.dtos.CategoryDTO;


public class CategoryRequest extends PaginationGenericRequest{

	
	//private Attributes
	////////////////////
	
	private CategoryDTO categoryObj;
	
	
	
	//public Attributes
	////////////////////
	
	public CategoryDTO getCategoryObj() {
		return categoryObj;
	}

	public void setCategoryObj(CategoryDTO categoryObj) {
		this.categoryObj = categoryObj;
	}



}
