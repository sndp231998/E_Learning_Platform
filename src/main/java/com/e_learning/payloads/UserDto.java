package com.e_learning.payloads;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
public class UserDto {
	private int id;

	@NotEmpty
	@Size(min = 4, message = "Username must be min of 4 characters !!")
	private String name;

	@Email(message = "Email address is not valid !!")
	@NotEmpty(message = "Email is required !!")
	private String email;

	@NotEmpty
	@Size(min = 3, max = 10, message = "Password must be min of 3 chars and max of 10 chars !!")
	private String password;

	@NotEmpty
	private String collegename;
	
	private String faculty;
	
	 @Column(name = "otp")
   private String otp;
	
	 
	 private String discount;

//	private LocalDateTime SubscriptionValidDate;
//	
//	private LocalDateTime date_Of_Role_Changed;
//	
//	private LocalDateTime lastNotificationDate;
	
	private Set<RoleDto> roles = new HashSet<>();
	
	//---------------------------------------
//    private String mobileNo;
//
//   
//
//    private LocalDateTime otpValidUntil;
    //------------------------------------------
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password=password;
	}
}
