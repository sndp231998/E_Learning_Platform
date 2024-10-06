package com.e_learning.services;

import java.util.List;

import com.e_learning.entities.Booked;
import com.e_learning.payloads.BookedDto;



public interface BookedService {
	BookedDto createBooked(BookedDto bookedDto,Integer userId,Integer categoryId);
	
	 List<BookedDto> getBookedsByCategory(Integer categoryId);
	 List<BookedDto>getBookedsByUserId(Integer userId);
	List<BookedDto> getAllBookeds();
	
}
