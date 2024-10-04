package com.e_learning.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e_learning.entities.Booked;
import com.e_learning.entities.Category;
import com.e_learning.entities.Post;
import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;
import com.e_learning.payloads.BookedDto;
import com.e_learning.payloads.CategoryDto;
import com.e_learning.payloads.PostDto;
import com.e_learning.repositories.CategoryRepo;
import com.e_learning.services.CategoryService;



@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;


	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		Category cat = this.modelMapper.map(categoryDto, Category.class);
		cat.setPrice(categoryDto.getPrice());
		cat.setMainCategory(categoryDto.getMainCategory());
		cat.setAddedDate(LocalDateTime.now());
		cat.setImageName("default.png");
		Category addedCat = this.categoryRepo.save(cat);
		return this.modelMapper.map(addedCat, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category ", "Category Id", categoryId));

		cat.setCategoryTitle(categoryDto.getCategoryTitle());
		cat.setCategoryDescription(categoryDto.getCategoryDescription());
         cat.setPrice(categoryDto.getPrice());
         cat.setAddedDate(LocalDateTime.now());
         cat.setImageName(categoryDto.getImageName());
         cat.setMainCategory(categoryDto.getMainCategory());
		Category updatedcat = this.categoryRepo.save(cat);

		return this.modelMapper.map(updatedcat, CategoryDto.class);
	}

	
	//Delete Category
	@Override
	public void deleteCategory(Integer categoryId) {

		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category ", "category id", categoryId));
		this.categoryRepo.delete(cat);
	}

	
	//GetCAtegory BY Id
	@Override
	public CategoryDto getCategory(Integer categoryId) {
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

		return this.modelMapper.map(cat, CategoryDto.class);
	}

	//GetAll CAtegory
	@Override
	public List<CategoryDto> getCategories() {

		List<Category> categories = this.categoryRepo.findAll();
		List<CategoryDto> catDtos = categories.stream().map((cat) -> this.modelMapper.map(cat, CategoryDto.class))
				.collect(Collectors.toList());

		return catDtos;
	}
	@Override
    public List<CategoryDto> getLatestCategories() {
        List<Category> categories = categoryRepo.findAllByOrderByAddedDateDesc();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }
	 @Override
	    public List<CategoryDto> searchByCategoryTitle(String categoryTitle) {
	        List<Category> categories = categoryRepo.findByCategoryTitleContainingIgnoreCase(categoryTitle);
	        if (categories.isEmpty()) {
	            throw new ResourceNotFoundException("Category", "title", categoryTitle);
	        }
	        return categories.stream()
	                .map(cat -> modelMapper.map(cat, CategoryDto.class))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public List<CategoryDto> searchByMainCategory(String mainCategory) {
	        List<Category> categories = categoryRepo.findByMainCategoryContainingIgnoreCase(mainCategory);
	        if (categories.isEmpty()) {
	            throw new ResourceNotFoundException("Category", "main category", mainCategory);
	        }
	        return categories.stream()
	                .map(cat -> modelMapper.map(cat, CategoryDto.class))
	                .collect(Collectors.toList());
	    }
	    
	    @Override
	    public List<CategoryDto> searchCategories(String keyword) {
	        List<Category> categories = categoryRepo.findByCategoryTitleOrMainCategoryContaining(keyword);
	        return categories.stream()
	                .map(category -> modelMapper.map(category, CategoryDto.class))
	                .collect(Collectors.toList());
	    }
	}


