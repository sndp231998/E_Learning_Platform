package com.e_learning.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.e_learning.payloads.ApiResponse;

import com.e_learning.payloads.ExamDto;
import com.e_learning.services.ExamService;
import com.e_learning.services.FileService;


@RestController
@RequestMapping("/api/v1/")
public class ExamController {
	@Autowired
	private ExamService examService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;
	
	//create
	@PostMapping("/user/{userId}/category/{categoryId}/exams")
	public ResponseEntity<ExamDto> createExam(@RequestBody ExamDto examDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		ExamDto createExam = this.examService.createExam(examDto, userId, categoryId);
		return new ResponseEntity<ExamDto>(createExam, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/user/{userId}/exams")
	public ResponseEntity<List<ExamDto>> getExamsByUser(@PathVariable Integer userId) {

		List<ExamDto> exams = this.examService.getExamsByUser(userId);
		return new ResponseEntity<List<ExamDto>>(exams, HttpStatus.OK);

	}
	
	@GetMapping("/category/{categoryId}/exams")
	public ResponseEntity<List<ExamDto>> getExamsByCategory(@PathVariable Integer categoryId) {

		List<ExamDto> exams = this.examService.getExamsByCategory(categoryId);
		return new ResponseEntity<List<ExamDto>>(exams, HttpStatus.OK);

	}
	
	//get Exams By userFaculty
	@PreAuthorize("hasRole('ADMIN') or hasRole('SUBSCRIBED')")
	 @GetMapping("exams/user/{userId}")
	    public ResponseEntity<List<ExamDto>> getExamsByUserFaculty(@PathVariable Integer userId) {
	        List<ExamDto> exams = this.examService.getExamsByUserFaculty(userId);
	        return new ResponseEntity<>(exams, HttpStatus.OK);
	    }
	
	// get all Exam
		@GetMapping("/")
		public ResponseEntity<List<ExamDto>> getExams() {
			List<ExamDto> exams = this.examService.getExams();
			return ResponseEntity.ok(exams);
		}
		
		// get Exam details by id

		@GetMapping("/exams/{examId}")
		public ResponseEntity<ExamDto> getExamById(@PathVariable Integer examId) {

			ExamDto examDto = this.examService.getExamById(examId);
			return new ResponseEntity<ExamDto>(examDto, HttpStatus.OK);

		}
		
		// delete post
		@DeleteMapping("/exams/{examId}")
		public ApiResponse deleteExam(@PathVariable Integer examId) {
			this.examService.deleteExam(examId);
			return new ApiResponse("Exam is successfully deleted !!", true);
		}
		
		// update post

		@PutMapping("/exams/{examId}")
		public ResponseEntity<ExamDto> updateExam(@RequestBody ExamDto examDto, @PathVariable Integer examId) {

			ExamDto updateExam = this.examService.updateExam(examDto, examId);
			return new ResponseEntity<ExamDto>(updateExam, HttpStatus.OK);

		}

		// search
		@GetMapping("/exams/search/{keywords}")
		public ResponseEntity<List<ExamDto>> searchExamByTitle(@PathVariable("keywords") String keywords) {
			List<ExamDto> result = this.examService.searchExams(keywords);
			return new ResponseEntity<List<ExamDto>>(result, HttpStatus.OK);
		}
		
		// post image upload

		@PostMapping("/exam/image/upload/{examId}")
		public ResponseEntity<ExamDto> uploadExamImage(@RequestParam("image") MultipartFile image,
				@PathVariable Integer examId) throws IOException {

			ExamDto examDto = this.examService.getExamById(examId);
			
			String fileName = this.fileService.uploadImage(path, image);
			examDto.setImageName(fileName);
			ExamDto updateExam = this.examService.updateExam(examDto, examId);
			return new ResponseEntity<ExamDto>(updateExam, HttpStatus.OK);

		}
		
		 //method to serve files
	    @GetMapping(value = "/exam/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
	    public void downloadImage(
	            @PathVariable("imageName") String imageName,
	            HttpServletResponse response
	    ) throws IOException {

	        InputStream resource = this.fileService.getResource(path, imageName);
	        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	        StreamUtils.copy(resource,response.getOutputStream())   ;

	    }
}