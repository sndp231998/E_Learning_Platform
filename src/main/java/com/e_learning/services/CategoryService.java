package com.e_learning.services;

import java.util.List;

import com.e_learning.payloads.CategoryDto;

public interface CategoryService {

	
	 List<CategoryDto> getLatestCategories();
	// create
		CategoryDto createCategory(CategoryDto categoryDto);

		// update
		CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);

		// delete
		void deleteCategory(Integer categoryId);

		// get
		CategoryDto getCategory(Integer categoryId);

		// get All

		List<CategoryDto> getCategories();
		
		//List<CategoryDto> getUsersByCategoryTitle(String title); 
		//-------search------------------
		List<CategoryDto> searchByCategoryTitle(String categoryTitle);

	    List<CategoryDto> searchByMainCategory(String mainCategory);
	    
	    List<CategoryDto> searchCategories(String keyword);

}
