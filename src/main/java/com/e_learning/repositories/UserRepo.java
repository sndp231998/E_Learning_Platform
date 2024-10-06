package com.e_learning.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.e_learning.entities.User;



public interface UserRepo extends JpaRepository<User, Integer>{
	
	Optional<User> findByEmail(String email);
	Optional<User> findByMobileNo(String mobileNo);

	List<User> findByCollegename(String collegename);


@Query("SELECT DISTINCT u.faculty FROM User u")
List<String> findAllFaculties();
}