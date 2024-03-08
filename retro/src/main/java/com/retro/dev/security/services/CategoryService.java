package com.retro.dev.security.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retro.dev.dtos.UserCategoryDTO;
import com.retro.dev.models.Category;
import com.retro.dev.models.SubCategory;
import com.retro.dev.models.User;
import com.retro.dev.models.UserCategory;
import com.retro.dev.payload.request.CategoryRequest;
import com.retro.dev.payload.response.CategoryResponse;
import com.retro.dev.repository.CategoryRepository;
import com.retro.dev.repository.SubCategoryRepository;
import com.retro.dev.repository.UserCategoryRepository;
import com.retro.dev.repository.UserRepository;
import com.retro.dev.util.PublicConstants;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryDao;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserCategoryRepository userCategoryDao;
    
    @Autowired
    SubCategoryRepository subCategoryDao;

    /*public ResponseEntity<?> save(String username, Category category) {
    	List<Category> categoryData = listCategory(username);
    	if(categoryData != null) {
    		for(Category eachCategory : categoryData) {
    			if(eachCategory.getCategory().equals(category.getCategory())) {
    				 return ResponseEntity.badRequest().body(new MessageResponse("category already exists!"));
    			}
    		}
    	}
    	Optional<User> user = userRepository.findByUsername(username);
        Category newCategory = new Category(user.get().getId(), category.getCategory(), category.getSubcategory());
        categoryDao.save(newCategory);
        return ResponseEntity.ok(new MessageResponse("category "+HttpStatus.ACCEPTED));
    }

    public ResponseEntity<?> update(String username, Category pcat) {
        Category cat = categoryDao.findById(pcat.getId());
        cat.setUserid(cat.getUserid());
        cat.setCategory(pcat.getCategory());
        cat.setSubcategory(pcat.getSubcategory());

        Category updatedCategory = categoryDao.save(cat);
        if (updatedCategory != null)
            return ResponseEntity.ok(new MessageResponse("Category updated"));
        else
            return ResponseEntity.badRequest().body(new MessageResponse("failed to update the category"));
    }


    public List<Category> listCategory(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return categoryDao.findAllByUserid(user.get().getId());

    }

    public List<CategoryResponse> listAllCategory(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.get().getId() != null) {
             List<Category> result = categoryDao.findAll();
            List<CategoryResponse> conv = result.stream()
                    .map(p -> new CategoryResponse(p.getCategory(),p.getSubcategory()))
                    .collect(Collectors.toList());

            return conv;

        }
       return null;
    }*/

	public Category findCategoryById(Long categoryId) {
		Category cat = categoryDao.findByCategoryId(categoryId);
		return cat;
	}

	public CategoryResponse listCategoryByUserId(Principal principal, Long userId) {
		//Optional<User> user = userRepository.findByUsername(principal.getName());
		CategoryResponse response = new CategoryResponse();
		List<UserCategory> userCategoryList = userCategoryDao.findAllByUserId(userId);
		response.setUserCategoryList(userCategoryList);
        return generateUserCategorySuccessResp(response, "User Categories fetched successfully");
	}

	public CategoryResponse listAllCategory(Principal principal) {
		CategoryResponse response = new CategoryResponse();
		List<Category> allCategoriesList = categoryDao.findAll();
		response.setCategoryList(allCategoriesList);
		return generateUserCategorySuccessResp(response, "All Categories fetched successfully");
	}

	public CategoryResponse listAllSubCategory(Principal principal) {
		CategoryResponse response = new CategoryResponse();
		List<SubCategory> allSubCategoriesList = subCategoryDao.findAll();
		response.setSubCategoryList(allSubCategoriesList);
		return generateUserCategorySuccessResp(response, "All SubCategories fetched successfully");
	}
	

	public CategoryResponse createCategory(Principal principal, CategoryRequest categoryReq) {
		CategoryResponse response = new CategoryResponse();
		Category fetchedCategory = categoryDao.findByCategory(categoryReq.getCategoryObj().getCategory());
		if(null!=fetchedCategory) {
			return generateFailResponse("this category already exists!");
		}
		Category categoryToSave = new Category();
		BeanUtils.copyProperties(categoryReq.getCategoryObj(), categoryToSave);
		categoryToSave = categoryDao.save(categoryToSave);
		response.setCategoryObj(categoryToSave);
		return generateCategorySuccessResp(response, "Category added.");
	}

	public CategoryResponse getCategoryDetail(Principal principal, Long categoryId) {
		CategoryResponse response = new CategoryResponse();
		Category categoryDetails = categoryDao.findByCategoryId(categoryId);
		response.setCategoryObj(categoryDetails);
		return generateCategorySuccessResp(response, "Category details fetched successfully.");
	}
	
	
	public CategoryResponse createSubCategory(Principal principal, CategoryRequest categoryReq) {
		CategoryResponse response = new CategoryResponse();
		SubCategory fetchedSubCategory = subCategoryDao.findBySubcategory(categoryReq.getCategoryObj().getSubcategory());
		if(null!=fetchedSubCategory) {
			return generateFailResponse("this Sub-Category already exists!");
		}
		SubCategory subCategoryToSave = new SubCategory();
		BeanUtils.copyProperties(categoryReq.getCategoryObj(), subCategoryToSave);
		subCategoryToSave.setCategory(categoryReq.getCategoryObj().getCategoryObj());
		subCategoryToSave = subCategoryDao.save(subCategoryToSave);
		response.setSubcategoryObj(subCategoryToSave);
		return generateCategorySuccessResp(response, "SubCategory added.");
	}

	public CategoryResponse getSubCategoryDetail(Principal principal, Long subcategoryId) {
		CategoryResponse response = new CategoryResponse();
		SubCategory subCategoryDetails = subCategoryDao.findBySubcategoryId(subcategoryId);
		response.setSubcategoryObj(subCategoryDetails);
		return generateCategorySuccessResp(response, "SubCategory details fetched successfully.");
	}

	public CategoryResponse getAllSubCategoriesBasedOnCategoryId(Principal principal, Long categoryId) {
		CategoryResponse response = new CategoryResponse();
		Category newTempCategory = new Category();
		newTempCategory.setCategoryId(categoryId);
		List<SubCategory> subcategoriesList = subCategoryDao.findByCategory(newTempCategory);
		if(subcategoriesList!=null && subcategoriesList.size()<1) {
			return generateFailResponse("Sub-Category does not exist for this category!");
		}
		response.setSubCategoryList(subcategoriesList);
		return generateCategorySuccessResp(response, "SubCategories fetched successfully.");
	}


	public CategoryResponse createUserCategory(Principal principal, CategoryRequest categoryReq) {
		CategoryResponse response = new CategoryResponse();
		response = validateRequestData(categoryReq);
		if(null!=response && StringUtils.isNotBlank(response.getResponseStatus()) 
				&& response.getResponseStatus().equals(PublicConstants.FAIL)) {
			return response;
		}
		Optional<User> user = userRepository.findByUsername(principal.getName());
		List<UserCategory> userCategoryList = userCategoryDao.findAllByUserId(user.get().getId());
		if(null!=userCategoryList && userCategoryList.size()>9) {
			return generateFailResponse("Max 10 Categories allowed per user.");
		}
		for(UserCategoryDTO eachUserCat : categoryReq.getCategoryObj().getUserCategoryList()) {
			UserCategory userCategoryEntity = new UserCategory();
			BeanUtils.copyProperties(categoryReq.getCategoryObj(), userCategoryEntity);
			userCategoryEntity.setCategory(eachUserCat.getCategory());
			userCategoryEntity.setSubcategory(eachUserCat.getSubcategory());
			userCategoryEntity = userCategoryDao.save(userCategoryEntity);
			if(null==userCategoryList) {
				userCategoryList = new ArrayList<UserCategory>();}
			userCategoryList.add(userCategoryEntity);
		}
		response.setUserCategoryList(userCategoryList);
		return generateUserCategorySuccessResp(response, "User Category successfully added.");
	}

	
	private CategoryResponse validateRequestData(CategoryRequest categoryReq) {
		CategoryResponse response = new CategoryResponse();
		// validate the request Category obj and userId
		return response;
	}


	public CategoryResponse getUserCategoryDetail(Principal principal, Long userId, Long userCategoryId) {
		CategoryResponse response = new CategoryResponse();
		UserCategory userCategoryDetail = userCategoryDao.findByUserCategoryId(userCategoryId);
		response.setUserCategoryObj(userCategoryDetail);
		return generateUserCategorySuccessResp(response, "User Category successfully fetched.");
	}

	public CategoryResponse updateUserCategory(Principal principal, CategoryRequest categoryReq) {
		CategoryResponse response = new CategoryResponse();
		UserCategory userCategoryDetail = userCategoryDao.findByUserCategoryId(categoryReq.getCategoryObj().getUserCategoryId());
		if(null==userCategoryDetail) {
			return generateFailResponse("Category cannot be updated as Category not found.");
		}
		userCategoryDetail.setCategory(categoryReq.getCategoryObj().getCategoryObj());
		userCategoryDetail.setSubcategory(categoryReq.getCategoryObj().getSubcategoryObj());
		userCategoryDetail.setLastUpdatedDate(categoryReq.getCategoryObj().getLastUpdatedDate());
		userCategoryDetail = userCategoryDao.save(userCategoryDetail);
		response.setUserCategoryObj(userCategoryDetail);
		return generateUserCategorySuccessResp(response, "User Category updated successfully.");
	}
	
	public CategoryResponse generateUserCategorySuccessResp(CategoryResponse response, String msg) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}
	
	public CategoryResponse generateCategorySuccessResp(CategoryResponse response, String msg) {
		response.setResponseStatus(PublicConstants.SUCCESS);
		response.setResponseDesc(msg);
		return response;
	}

	public CategoryResponse generateFailResponse(String msg) {
		CategoryResponse response = new CategoryResponse();
		response.setResponseStatus(PublicConstants.FAIL);
		response.setResponseDesc(msg);
		return response;
	}

}
