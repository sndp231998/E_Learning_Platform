package com.e_learning.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.e_learning.entities.Category;



public interface CategoryRepo extends JpaRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.categoryTitle = :categoryTitle")
	Category findByCategoryTitle(@Param("categoryTitle") String categoryTitle);

	List<Category> findAllByOrderByAddedDateDesc();
	
	// Search by category title
    List<Category> findByCategoryTitleContainingIgnoreCase(String categoryTitle);

    // Search by main category
    List<Category> findByMainCategoryContainingIgnoreCase(String mainCategory);
    
    @Query("SELECT c FROM Category c WHERE LOWER(c.categoryTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(c.mainCategory) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    	List<Category> findByCategoryTitleOrMainCategoryContaining(@Param("keyword") String keyword);


   
}
