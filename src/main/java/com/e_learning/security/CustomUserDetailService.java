package com.e_learning.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;
import com.e_learning.repositories.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// loading user from database by username
		User user = this.userRepo.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User ", " email : " + username, 0));

		return user;
	}
	
	// New method to load by mobile number
    public UserDetails loadUserByMobile(String mobileNum) throws UsernameNotFoundException {
        // loading user from database by mobile number
        User user = this.userRepo.findByMobileNo(mobileNum)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile number: " + mobileNum));

        return user;
    }


}
