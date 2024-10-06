package com.e_learning.payloads;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

	private String name;


	private String email;
 

	private String password;

	private String collegename;
	private String imageName;
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
