package com.e_learning.services;

import com.e_learning.entities.ForgetPassword;
import com.e_learning.entities.OtpRequest;
import com.e_learning.payloads.ForgetPasswordDto;

public interface ForgetPasswordService {

	//create for reg
	ForgetPassword createForget(ForgetPasswordDto pas);
	
	// Update user password after OTP verification
    void updatePassword(String mobileNo, String otp, String newPassword);
}
