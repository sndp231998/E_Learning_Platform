package com.e_learning.services;

import com.e_learning.entities.OtpRequest;


public interface OtpRequestService {

	      //create for reg
			OtpRequest createOtp(OtpRequest otpReq);
			
			 // Send OTP
		    OtpRequest SendOtp(OtpRequest otpReq, String phnumber);

			void sendMessage(String mobileNo,String message);
			
			 void sendOtpSms(String mobileNo,String otp);
}
