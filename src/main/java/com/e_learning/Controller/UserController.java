package com.e_learning.Controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.e_learning.payloads.ApiResponse;
import com.e_learning.payloads.UserDto;
import com.e_learning.services.UserService;
import com.e_learning.services.impl.RateLimitingService;
import com.e_learning.services.impl.UserServiceImpl;




@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	 private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserService userService;
	
	 @Autowired
	    private RateLimitingService rateLimitingService;

	// POST-create user
	 @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto createUserDto = this.userService.createUser(userDto);
		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
	}

	// PUT- update user
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer uid) {
		UserDto updatedUser = this.userService.updateUser(userDto, uid);
		return ResponseEntity.ok(updatedUser);
	}

	//ADMIN
	// DELETE -delete user
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid) {
		this.userService.deleteUser(uid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted Successfully", true), HttpStatus.OK);
	}

	// GET - user get
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		 rateLimitingService.checkRateLimit("test-api-key");
		return ResponseEntity.ok(this.userService.getAllUsers());
	}

	// GET - user get
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId) {
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/college/{collegename}")
    public ResponseEntity<List<UserDto>> getUsersByCollegeName(@PathVariable String collegename) {
        List<UserDto> users = userService.getUsersByCollegeName(collegename);
        return ResponseEntity.ok(users);
    }
	
	//-----------------ROles change----------------
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addRole/email/{email}/role/{roleName}")
	public ResponseEntity<ApiResponse> addRoleToUser(@PathVariable String email, @PathVariable String roleName) {
	    	    userService.addRoleToUser(email, roleName);
	    ApiResponse response = new ApiResponse("Role added successfully", true);
	    return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
	
	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String roleName) {
        List<UserDto> users = userService.getUsersByRole(roleName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
//------Faculty add and update ----
 
 // Update Faculty API
//    @PutMapping("/{userId}/faculty")
//    public ResponseEntity<UserDto> updateFaculty(
//            @PathVariable Integer userId, 
//            @RequestParam String faculty) {
//        UserDto updatedUser = userService.updateFaculty(userId, faculty);
//        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//    }

 
//    // Update Discount API
//    @PutMapping("/{userId}/discount")
//    public ResponseEntity<UserDto> updateDiscount(
//            @PathVariable Integer userId, 
//            @RequestParam String discount) {
//        UserDto updatedUser = userService.updateDiscount(userId, discount);
//        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//    }
	
	
	//@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{userId}/discount")
	public ResponseEntity<UserDto> updateDiscount(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer uid) {
		UserDto updatedDiscount = this.userService.updateDiscount(userDto, uid);
		String a=userDto.getDiscount();
		logger.info("discount form controller ..........................."+a);
		return ResponseEntity.ok(updatedDiscount);
	}
	
	@PutMapping("/{userId}/faculty")
	public ResponseEntity<UserDto> updateFaculty(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer uid) {
		UserDto updatedfaculty = this.userService.updateFaculty(userDto, uid);
		String a=userDto.getFaculty();
		logger.info("faculty form controller ..........................."+a);
		return ResponseEntity.ok(updatedfaculty);
	}
}
