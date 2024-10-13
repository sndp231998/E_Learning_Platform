package com.e_learning.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e_learning.entities.Booked;
import com.e_learning.entities.Category;
import com.e_learning.entities.Payment;
import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;

import com.e_learning.payloads.PaymentDto;
import com.e_learning.repositories.CategoryRepo;
import com.e_learning.repositories.PaymentRepo;
import com.e_learning.repositories.UserRepo;
import com.e_learning.services.PaymentService;
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto, Integer userId, List<Integer> categoryIds) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new IllegalArgumentException("Category IDs cannot be null or empty.");
        }

        List<Category> categories = categoryRepo.findAllById(categoryIds);

        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Categories", "Category IDs", categoryIds.toString());
        }

        // Check if user has already purchased any requested categories
        List<Category> purchasedCategories = paymentRepo.findCategoriesByUserId(userId);
        List<Integer> purchasedCategoryIds = purchasedCategories.stream()
                .map(Category::getCategoryId)
                .collect(Collectors.toList());

        List<Integer> alreadyPurchased = categoryIds.stream()
                .filter(purchasedCategoryIds::contains)
                .collect(Collectors.toList());

        if (!alreadyPurchased.isEmpty()) {
            throw new IllegalArgumentException("User has already purchased categories: " + alreadyPurchased);
        }

        // Calculate total price
        int totalPrice = categories.stream()
                .mapToInt(category -> Integer.parseInt(category.getPrice()))
                .sum();

        // Map paymentDto to Payment
        Payment payment = modelMapper.map(paymentDto, Payment.class);
        payment.setUser(user);
        payment.setTotalPrice(totalPrice);
        payment.setAddedDate(LocalDateTime.now());
        payment.setCategories(categories);

        Payment newPayment = paymentRepo.save(payment);
        return modelMapper.map(newPayment, PaymentDto.class);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepo.findAll().stream()
                .map(payment -> modelMapper.map(payment, PaymentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCategoryPaymentByUser(Integer userId, Integer categoryId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));

        return paymentRepo.findByUserAndCategory(user, category).isPresent();
    }
}

//@Service
//public class PaymentServiceImpl implements PaymentService{
//
//	@Autowired
//    private PaymentRepo paymentRepo;
//	@Autowired
//	private CategoryRepo categoryRepo;
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private UserRepo userRepo;
//    
//    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
//    
//    @Override
//    public PaymentDto createPayment(PaymentDto paymentDto, Integer userId, List<Integer> categoryIds) {
//        // Fetch the user
//        User user = this.userRepo.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
//
//        // Check if categoryIds are provided
//        if (categoryIds == null || categoryIds.isEmpty()) {
//            throw new IllegalArgumentException("Category IDs cannot be null or empty.");
//        }
//        logger.info("List of category IDs: " + categoryIds);
//
//        // Fetch the selected categories
//        List<Category> categories = this.categoryRepo.findAllById(categoryIds);
//
//        // Log fetched categories
//        System.out.println("Fetched Categories: " + categories);
//
//        // Check if categories were found
//        if (categories.isEmpty()) {
//            throw new ResourceNotFoundException("Categories", "Category IDs", categoryIds.toString());
//        }
//
//        // Check if user already has purchased any of the requested categories
//        List<Category> purchasedCategories = this.paymentRepo.findCategoriesByUserId(userId);
//        List<Integer> purchasedCategoryIds = purchasedCategories.stream()
//                .map(Category::getCategoryId)
//                .collect(Collectors.toList());
//
//        // Find intersection of requested and purchased categories
//        List<Integer> alreadyPurchased = categoryIds.stream()
//                .filter(purchasedCategoryIds::contains)
//                .collect(Collectors.toList());
//
//        if (!alreadyPurchased.isEmpty()) {
//            throw new IllegalArgumentException("User has already purchased categories: " + alreadyPurchased);
//        }
//
//        // Calculate total price by summing up the prices of the selected categories
//        int totalPrice = categories.stream()
//                .mapToInt(category -> {
//                    try {
//                        return Integer.parseInt(category.getPrice());
//                    } catch (NumberFormatException e) {
//                        throw new IllegalArgumentException("Invalid price format for category: " + category.getCategoryId());
//                    }
//                })
//                .sum();
//
//        // Map paymentDto to Payment entity
//        Payment payment = this.modelMapper.map(paymentDto, Payment.class);
//
//        // Set user and total price
//        payment.setUser(user);
//        payment.setTotalPrice(totalPrice);
//        payment.setAddedDate(LocalDateTime.now());
//        payment.setPayment_screensort("");
//        
//        // Set the list of categories
//        payment.setCategories(categories); // Set multiple categories here
//
//        // Save the payment
//        Payment newPayment = this.paymentRepo.save(payment);
//
//        // Return the PaymentDto mapped from the newly created payment entity
//        return this.modelMapper.map(newPayment, PaymentDto.class);
//    }
//
//	@Override
//	public List<PaymentDto> getAllPayments() {
//		List<Payment> pay = this.paymentRepo.findAll();
//		List<PaymentDto> payDtos = pay.stream().map((exa) -> this.modelMapper.map(exa, PaymentDto.class))
//				.collect(Collectors.toList());
//
//		return payDtos;
//	}
//
//	
//	@Override
//	 public boolean isCategoryPaymentByUser(Integer userId, Integer categoryId) {
//	        User user = this.userRepo.findById(userId)
//	                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
//	        
//	        Category category = this.categoryRepo.findById(categoryId)
//	                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));
//	        
//	        Optional<Payment> existing = this.paymentRepo.findByUserAndCategory(user, category);
//	        return existing.isPresent();
//	    }
//	
//	
//	
//	
//}
