package com.e_learning.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e_learning.entities.Payment;
import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;

import com.e_learning.payloads.PaymentDto;

import com.e_learning.repositories.PaymentRepo;
import com.e_learning.repositories.UserRepo;
import com.e_learning.services.PaymentService;
@Service
public class PaymentServiceImpl implements PaymentService{

	@Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

	@Override
	public PaymentDto createPayment(PaymentDto paymentDto, Integer userId) {
		 User user = this.userRepo.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));
		 Payment payment = this.modelMapper.map(paymentDto, Payment.class);
	 
          payment.setPayment_screensort("");
          payment.setUser(user);
          payment.setAddedDate(LocalDateTime.now());
          
	        Payment newpayment = this.paymentRepo.save(payment);

	        return this.modelMapper.map(newpayment, PaymentDto.class);
	    
	}

	@Override
	public List<PaymentDto> getAllPayments() {
		List<Payment> pay = this.paymentRepo.findAll();
		List<PaymentDto> payDtos = pay.stream().map((exa) -> this.modelMapper.map(exa, PaymentDto.class))
				.collect(Collectors.toList());

		return payDtos;
	}

}
