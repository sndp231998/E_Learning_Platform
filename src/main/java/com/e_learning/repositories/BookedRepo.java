package com.e_learning.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.e_learning.entities.Booked;
import com.e_learning.entities.Category;
import com.e_learning.entities.Post;
import com.e_learning.entities.User;


public interface BookedRepo extends JpaRepository<Booked, Integer> {
    List<Booked> findByUserId(Integer userId);

   
	List<Booked> findByCategory(Integer categoryId);
	Optional<Booked> findByUserAndCategory(User user, Category category);
}

