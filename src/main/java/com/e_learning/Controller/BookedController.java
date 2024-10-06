package com.e_learning.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_learning.entities.User;
import com.e_learning.payloads.BookedDto;
import com.e_learning.payloads.PostDto;
import com.e_learning.payloads.UserDto;
import com.e_learning.services.BookedService;


@RestController
@RequestMapping("/api/v1/")
public class BookedController {

	@Autowired
	private BookedService bookedService;


	// create
	//@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/user/{userId}/category/{categoryId}/bookeds")
	public ResponseEntity<BookedDto> createPost(@RequestBody BookedDto bookedDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		
		
		 try {
	            BookedDto createdBooked = this.bookedService.createBooked(bookedDto, userId, categoryId);
	            return new ResponseEntity<>(createdBooked, HttpStatus.CREATED);
	        } catch (IllegalStateException e) {
	            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	        }
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/bookeds")
	public ResponseEntity<List<BookedDto>> getAllBookeds() {
		 
		return ResponseEntity.ok(this.bookedService.getAllBookeds());
	}
	
	@GetMapping("booked/user/{userId}")
    public ResponseEntity<List<BookedDto>> getBookedCoursesByUserId(@PathVariable Integer userId) {
        List<BookedDto> bookedCourses = bookedService.getBookedsByCategory(userId);
        		
        return ResponseEntity.ok(bookedCourses);
    }
}
