package com.e_learning.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_learning.entities.Booked;


public interface BookedRepo extends JpaRepository<Booked, Integer> {
	List<Booked> findByUserId(Integer userId);
}
