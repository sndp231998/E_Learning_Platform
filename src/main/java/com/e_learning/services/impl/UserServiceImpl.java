package com.e_learning.services.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.e_learning.config.AppConstants;
import com.e_learning.entities.OtpRequest;
import com.e_learning.entities.Payment;
import com.e_learning.entities.Role;
import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;
import com.e_learning.payloads.UserDto;
import com.e_learning.repositories.OtpRequestRepo;
import com.e_learning.repositories.PaymentRepo;
import com.e_learning.repositories.RoleRepo;
import com.e_learning.repositories.UserRepo;
import com.e_learning.services.OtpRequestService;
import com.e_learning.services.UserService;


@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OtpRequestRepo otpRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private NotificationService notificationService;

    @Autowired
private OtpRequestService sendmsg;
    
    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
     // encoded the password
     		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
     		// roles
    		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
    		user.getRoles().add(role);
        String otp = userDto.getOtp();
        
        // Logging OTP from user
        logger.info("Otp from user: " + otp);
        
        if (otp == null) {
            throw new IllegalArgumentException("OTP must be provided");
        }

        // Get OTP requests from the repository
        List<OtpRequest> otpRequests = this.otpRepo.findByOtp(otp);
        logger.info("Retrieved OTP requests: " + otpRequests);
        
        // Iterate through each OTP request and check validity
        OtpRequest validOtpRequest = null;
        for (OtpRequest otpRequest : otpRequests) {
            // Null check before comparison
            if (otpRequest.getOtp() != null && otpRequest.getOtp().equals(otp)) {
                LocalDateTime otpValidUntil = otpRequest.getOtpValidUntil();
                if (otpValidUntil != null) {
                    Instant otpValidUntilInstant = otpValidUntil.atZone(ZoneId.systemDefault()).toInstant();
                    Instant now = Instant.now();
                    if (otpValidUntilInstant.isAfter(now)) {
                        validOtpRequest = otpRequest;
                        break; // Exit the loop if a valid OTP is found
                    }
                }
            }
        }
        
        if (validOtpRequest == null) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }
        
        String mobileNo = validOtpRequest.getMobileNo();
        user.setMobileNo(mobileNo);
        
        User newUser = this.userRepo.save(user);
        
        String welcomeMessage = String.format("Welcome, %s! We're excited to have you on our eLearning platform. Dive in and enjoy the journey ahead! "
        		+ "Thank you for choosing us, Utkrista Shikshya", user.getName());
        sendmsg.sendMessage(user.getMobileNo(), welcomeMessage); // Assuming notificationService sends SMS

        return this.modelMapper.map(newUser, UserDto.class);
    }
    
    
    
    
  
    //--------------------------forget password----------------
    //--------------------------get otp from user --------
    @Override
    public UserDto GetOtp(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        // Saving OTP to User table (optional step)
        user.setOtp(userDto.getOtp());
        
        // Save and return updated user with OTP
        return modelMapper.map(userRepo.save(user), UserDto.class);
    }
    
    //forget password 
	@Override
	public UserDto updatePassword(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

		String otp=userDto.getOtp();
		if(otp==null) {
			throw new IllegalArgumentException("OTP must be provided");
		}
		// Fetch the OTP from the database
	    List<OtpRequest> otpRequests = this.otpRepo.findByOtp(otp);
	    OtpRequest validOtpRequest = null;
	    for (OtpRequest otpRequest : otpRequests) {
	        if (otpRequest.getOtp() != null && otpRequest.getOtp().equals(otp)) {
	            LocalDateTime otpValidUntil = otpRequest.getOtpValidUntil();
	            if (otpValidUntil != null) {
	                Instant otpValidUntilInstant = otpValidUntil.atZone(ZoneId.systemDefault()).toInstant();
	                Instant now = Instant.now();
	                if (otpValidUntilInstant.isAfter(now)) {
	                    validOtpRequest = otpRequest;
	                    break; // Found valid OTP, exit loop
	                }
	            }
	        }
	    }
	    if (validOtpRequest == null) {
	        throw new IllegalArgumentException("Invalid or expired OTP");
	    }
	    // OTP is valid, proceed to update the password
	    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
	    User updatedUser = userRepo.save(user);

	    return modelMapper.map(updatedUser, UserDto.class);
		
	}
    

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setCollegename(userDto.getCollegename());

        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }

    
    
    
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }


    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        return users.stream().map(this::userToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        this.userRepo.delete(user);
    }

    public User dtoToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

    public UserDto userToDto(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }

    
 
    // other methods


    @Override
    public List<UserDto> getUsersByCollegeName(String collegename) {
        List<User> users = userRepo.findByCollegename(collegename);
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        // Fetch user by email, throw exception if not found
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Fetch role by name, throw exception if not found
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));

        // Check if role change is to "teacher" (no payment required)
        if (roleName.equalsIgnoreCase("Role_TEACHER")) {
            user.getRoles().clear();  // Clear existing roles
            user.getRoles().add(role);  // Add "teacher" role
            user.setDate_Of_Role_Changed(LocalDateTime.now());  // Update role change date
            userRepo.save(user);  // Save user
            System.out.println("User role changed to Teacher.");
            return;  // Return after role change
        }

        // Check if role change is to "subscribed" (payment required)
        if (roleName.equalsIgnoreCase("ROLE_SUBSCRIBED")) {
            List<Payment> payments = paymentRepo.findByUser(user);
            
            // If no payments found, throw exception
            if (payments == null || payments.isEmpty()) {
                throw new ResourceNotFoundException("Payment", "user", email);
            }

            // Get the latest payment
            Payment latestPayment = payments.get(0);

            // Ensure payment is valid (you can add more conditions if needed)
            if (LocalDateTime.now().isAfter(LocalDateTime.parse(latestPayment.getValidDate(), FORMATTER))) {
                throw new IllegalStateException("Payment is invalid or expired.");
            }

            // Clear existing roles and set "subscribed" role
            user.getRoles().clear();
            user.getRoles().add(role);
            user.setDate_Of_Role_Changed(LocalDateTime.now());
            user.setSubscriptionValidDate(LocalDateTime.parse(latestPayment.getValidDate(), FORMATTER));  // Set valid date

            userRepo.save(user);  // Save user
            System.out.println("User role changed to Subscribed after valid payment.");
            return;
        }

        // Throw exception if roleName is neither "teacher" nor "subscribed"
        throw new IllegalArgumentException("Invalid role change request.");
    }


    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getUsersByRole(String roleName) {
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
        return userRepo.findAll().stream()
                .filter(user -> user.getRoles().contains(role))
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
//1800000==30 min
    @Override
    @Scheduled(fixedRate =120000) // Runs every 2 minutes
    public void updateUserRoles() {
        logger.info("updateUserRoles method started");
        List<User> users = userRepo.findAll();
        logger.info("Number of users found: {}", users.size());

        for (User user : users) {
            if (user.getSubscriptionValidDate() != null) { 
                LocalDateTime validDate = user.getSubscriptionValidDate();
                logger.info("Processing user: {}, Subscription Valid Date: {}", user.getEmail(), validDate);

                for (Role role : user.getRoles()) {
                    LocalDateTime roleChangeDate = user.getDate_Of_Role_Changed();
                    logger.info("User role: {}, Role Change Date: {}", role.getName(), roleChangeDate);

                    // Check if current date is after the valid date
                    if (roleChangeDate != null && LocalDateTime.now().isAfter(validDate)) {
                        logger.info("Conditions met for user: {}, Role: {}", user.getEmail(), role.getName());

                        // Remove old role
                        user.getRoles().clear();
                        logger.info("Cleared old roles for user: {}", user.getEmail());

                        // Add new role
                        Role newRole = this.roleRepo.findById(AppConstants.NORMAL_USER)
                                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", AppConstants.NORMAL_USER));

                        logger.info("Added new role: {} for user: {}", newRole.getName(), user.getEmail());
                        user.getRoles().add(newRole);

                        // Clear the subscription valid date
                        user.setSubscriptionValidDate(null);
                        logger.info("Cleared subscription valid date for user: {}", user.getEmail());

                        userRepo.save(user);
                        logger.info("User roles updated and saved for user: {}", user.getEmail());
                    } else {
                        logger.info("Conditions not met for user: {}, Role: {}", user.getEmail(), role.getName());
                    }
                }
            } else {
                logger.info("User {} does not have a subscription valid date", user.getEmail());
            }
        }

        logger.info("updateUserRoles method completed");
    }

    @Override
    @Scheduled(fixedRate = 86400000) // Runs daily
    public void sendSubscriptionExpiryWarnings() {
        logger.info("sendSubscriptionExpiryWarnings method started");

        List<User> users = userRepo.findAll();
        for (User user : users) {
            if (user.getSubscriptionValidDate() != null) {
                LocalDateTime validDate = user.getSubscriptionValidDate();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime warningDate = validDate.minusDays(5);

                if (now.isAfter(warningDate) && now.isBefore(validDate)) {
                    String message = "Your subscription is ending soon. Please renew your subscription to continue enjoying our services.";
                    notificationService.sendNotification(user.getName(), message);
                    logger.info("Created warning notification for user: {}, Subscription Valid Date: {}", user.getEmail(), validDate);
                }
            }
        }

        logger.info("sendSubscriptionExpiryWarnings method completed");
    }



//---------------Update-Faculty--------------------------------------------------------
    @Override
    public UserDto updateFaculty(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        
        user.setFaculty(userDto.getFaculty());
        logger.info("Faculty from service "+userDto.getFaculty());
        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }
    //----------update discount only-------------
    @Override
    public UserDto updateDiscount(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        
        user.setDiscount(userDto.getDiscount());
        logger.info("discount from service "+userDto.getDiscount());
        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }
    
        
    }

	

