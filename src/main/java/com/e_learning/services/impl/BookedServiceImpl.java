package com.e_learning.services.impl;

import java.util.List;
import java.util.Optional;
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
	        // Fetch the user
	        User user = this.userRepo.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

	        // Fetch the category
	        Category category = this.categoryRepo.findById(categoryId)
	                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));

	     // Check if the user already booked the category
	        Optional<Booked> existingBooking = this.bookedRepo.findByUserAndCategory(user, category);
	        if (existingBooking.isPresent()) {
	            throw new IllegalArgumentException("User has already booked this category.");
	        }
	        // Proceed with creating the booking
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
	    public List<BookedDto> getBookedsByUserId(Integer userId) {
	        List<Booked> bookedCourses = bookedRepo.findByUserId(userId);
	        if (bookedCourses.isEmpty()) {
	            throw new ResourceNotFoundException("Booked Courses", "User id", userId);
	        }
	        return bookedCourses.stream()
	                .map(this::bookedToDto)
	                .collect(Collectors.toList());
	    }

		@Override
		public List<BookedDto> getBookedsByCategory(Integer categoryId) {
			List<Booked>bookedCourses=bookedRepo.findByCategory(categoryId);
			if(bookedCourses.isEmpty()) {
				 return bookedCourses.stream()
			                .map(this::bookedToDto)
			                .collect(Collectors.toList());
			}
			return null;
		}

	
	      
}
