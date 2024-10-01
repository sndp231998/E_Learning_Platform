package com.e_learning.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.e_learning.entities.Category;
import com.e_learning.entities.LiveStreaming;
import com.e_learning.entities.Post;
import com.e_learning.entities.User;




public interface LiveStreamingRepo extends JpaRepository<LiveStreaming, Integer>{

	List<LiveStreaming> findByCategory(Category category);
	
	List<LiveStreaming> findByUser(User user);
	
	
}
