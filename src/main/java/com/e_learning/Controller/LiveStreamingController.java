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
import com.e_learning.payloads.LiveStreamingDto;

import com.e_learning.services.LiveStreamingService;


@RestController
@RequestMapping("/api/v1/")
public class LiveStreamingController {

	@Autowired
	private LiveStreamingService liveService;
	
//	create
	@PostMapping("/user/{userId}/category/{categoryId}/lives")
	public ResponseEntity<LiveStreamingDto> createLiveStreaming(@RequestBody LiveStreamingDto liveDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		
		// Extract the user ID from the authentication context
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    User userDetails = (User) authentication.getPrincipal();
	    Integer tokenUserId = userDetails.getId(); // Get the user ID from the token


	    // Compare the user ID from the token with the user ID from the path variable
	    if (!tokenUserId.equals(userId)) {
	        return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
	    }
		LiveStreamingDto createLive = this.liveService.createLiveStreaming(liveDto, userId, categoryId);
		return new ResponseEntity<LiveStreamingDto>(createLive, HttpStatus.CREATED);
	}
	
	// get by category

		@GetMapping("/category/{categoryId}/lives")
		public ResponseEntity<List<LiveStreamingDto>> getLiveStreamingsByCategory(@PathVariable Integer categoryId) {

			List<LiveStreamingDto> lives = this.liveService.getLiveStreamingByCategory(categoryId);
			return new ResponseEntity<List<LiveStreamingDto>>(lives, HttpStatus.OK);

		}
//		get livestreamings By userFaculty
		@PreAuthorize("hasRole('ADMIN') or hasRole('SUBSCRIBED')")
		 @GetMapping("lives/user/{userId}")
		    public ResponseEntity<List<LiveStreamingDto>> getLivesByUserFaculty(@PathVariable Integer userId) {
		        List<LiveStreamingDto> lives = this.liveService.getLiveStreamingsByUserFaculty(userId);
		        return new ResponseEntity<>(lives, HttpStatus.OK);
		    }
}
