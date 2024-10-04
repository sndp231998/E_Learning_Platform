package com.e_learning.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e_learning.entities.Booked;
import com.e_learning.entities.Category;

import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;
import com.e_learning.payloads.BookedDto;
import com.e_learning.payloads.UserDto;
import com.e_learning.repositories.BookedRepo;
import com.e_learning.repositories.CategoryRepo;

import com.e_learning.repositories.UserRepo;
import com.e_learning.services.BookedService;

@Service
public class BookedServiceImpl implements BookedService{

	 @Autowired
	    private BookedRepo bookedRepo;

	    @Autowired
	    private ModelMapper modelMapper;

	    @Autowired
	    private UserRepo userRepo;

	    @Autowired
	    private CategoryRepo categoryRepo;

		@Override
		public BookedDto createBooked(BookedDto bookedDto, Integer userId, Integer categoryId) {
			 User user = this.userRepo.findById(userId)
		                .orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		        Category category = this.categoryRepo.findById(categoryId)
		                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id ", categoryId));

		        Booked booked = this.modelMapper.map(bookedDto, Booked.class);
		        booked.setUser(user);
		        booked.setCategory(category);
		        Booked newBooked = this.bookedRepo.save(booked);

		        return this.modelMapper.map(newBooked, BookedDto.class);

		}

		 public Booked dtoToBooked(BookedDto bookedDto) {
		        return this.modelMapper.map(bookedDto, Booked.class);
		    }

		    public BookedDto bookedToDto(Booked booked) {
		        return this.modelMapper.map(booked, BookedDto.class);
		    }
		@Override
		public List<BookedDto> getAllBookeds() {
			List<Booked>bookes=this.bookedRepo.findAll();
			
			return bookes.stream().map(this::bookedToDto).collect(Collectors.toList());

		}
		
		@Override
	    public List<BookedDto> getBookedCoursesByUserId(Integer userId) {
	        List<Booked> bookedCourses = bookedRepo.findByUserId(userId);
	        if (bookedCourses.isEmpty()) {
	            throw new ResourceNotFoundException("Booked Courses", "User id", userId);
	        }
	        return bookedCourses.stream()
	                .map(this::bookedToDto)
	                .collect(Collectors.toList());
	    }
	      
}
