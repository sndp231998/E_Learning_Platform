package com.e_learning.services.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e_learning.entities.Category;
import com.e_learning.entities.Exam;

import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;

import com.e_learning.payloads.ExamDto;

import com.e_learning.repositories.CategoryRepo;
import com.e_learning.repositories.ExamRepo;

import com.e_learning.repositories.UserRepo;
import com.e_learning.services.ExamService;
@Service
public class ExamServiceImpl implements ExamService{
	 @Autowired
	    private ExamRepo examRepo;

	    @Autowired
	    private ModelMapper modelMapper;

	    @Autowired
	    private UserRepo userRepo;

	    @Autowired
	    private CategoryRepo categoryRepo;
	      
	        
	@Override
	public ExamDto createExam(ExamDto examDto, Integer userId, Integer categoryId) {
		  User user = this.userRepo.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

	        Category category = this.categoryRepo.findById(categoryId)
	                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id ", categoryId));

	        Exam exam = this.modelMapper.map(examDto, Exam.class);
	        exam.setImageName("");
	        
	        exam.setAddedDate(LocalDateTime.now()); 
	        exam.setUser(user);
	        exam.setCategory(category);

	        Exam newexam = this.examRepo.save(exam);

	        return this.modelMapper.map(newexam, ExamDto.class);
	    

	}

	@Override
	public ExamDto updateExam(ExamDto examDto, Integer examId) {
		Exam exam = this.examRepo.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Examt ", "exam id", examId));

        Category category = this.categoryRepo.findById(examDto.getCategory().getCategoryId()).get();

        exam.setTitle(examDto.getTitle());
        exam.setDeadline(examDto.getDeadline());
        exam.setImageName(examDto.getImageName());
        exam.setCategory(category);


        Exam updatedexam = this.examRepo.save(exam);
        return this.modelMapper.map(updatedexam, ExamDto.class);
    }

	

	@Override
	public void deleteExam(Integer examId) {
		Exam exam = this.examRepo.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam ", "exam id", examId));

        this.examRepo.delete(exam);
		
	}

	

	@Override
	public ExamDto getExamById(Integer examId) {
		 Exam exam = this.examRepo.findById(examId)
	                .orElseThrow(() -> new ResourceNotFoundException("exam", "exam id", examId));
	        return this.modelMapper.map(exam, ExamDto.class);
	}

	@Override
	public List<ExamDto> getExamsByCategory(Integer categoryId) {
		 Category cat = this.categoryRepo.findById(categoryId)
	                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
	        List<Exam> exams = this.examRepo.findByCategory(cat);
	        		

	        List<ExamDto> examDtos = exams.stream().map((exam) -> this.modelMapper.map(exam, ExamDto.class))
	                .collect(Collectors.toList());

	        return examDtos;
	}

	@Override
	public List<ExamDto> getExamsByUser(Integer userId) {
		User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ", "userId ", userId));
        List<Exam> exams = this.examRepo.findByUser(user);

        List<ExamDto> examDtos = exams.stream().map((exam) -> this.modelMapper.map(exam, ExamDto.class))
                .collect(Collectors.toList());

        return examDtos;
	}
//yo user lai dekhaune
	@Override
	public List<ExamDto> getExamsByUserFaculty(Integer userId) {
		// Retrieve user by ID
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        
        // Get the user's faculty
        String userFaculty = user.getFaculty();

     // Find the category that matches the user's faculty
        Category category = this.categoryRepo.findByCategoryTitle(userFaculty);
        if (category == null) {
            throw new ResourceNotFoundException("Category", "title", userFaculty);
        }
        
     // Fetch posts associated with the category
        List<Exam> exams = this.examRepo.findByCategory(category);
     // Convert posts to PostDto
        List<ExamDto> examDtos = exams.stream()
                                      .map(exam -> this.modelMapper.map(exam, ExamDto.class))
                                      .collect(Collectors.toList());

        return examDtos;
	}

	@Override
	public List<ExamDto> searchExams(String keyword) {
		 List<Exam> exams = this.examRepo.searchByTitle("%" + keyword + "%");
	        List<ExamDto> examDtos = exams.stream().map((exam) -> this.modelMapper.map(exam, ExamDto.class)).collect(Collectors.toList());
	        return examDtos;
	}

	//get all
	@Override
	public List<ExamDto> getExams() {
		List<Exam> ex = this.examRepo.findAll();
		List<ExamDto> examDtos = ex.stream().map((exa) -> this.modelMapper.map(exa, ExamDto.class))
				.collect(Collectors.toList());

		return examDtos;
	}

}
