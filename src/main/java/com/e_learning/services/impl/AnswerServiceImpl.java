package com.e_learning.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e_learning.entities.Answer;
import com.e_learning.entities.Exam;
import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;
import com.e_learning.payloads.AnswerDto;
import com.e_learning.payloads.UserDto;
import com.e_learning.repositories.AnswerRepo;
import com.e_learning.repositories.ExamRepo;
import com.e_learning.repositories.UserRepo;
import com.e_learning.services.AnswerService;

@Service
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	private UserRepo userRepo;
    @Autowired
    private ExamRepo examRepo;

    @Autowired
    private AnswerRepo answerRepo;

    @Autowired
    private ModelMapper modelMapper;
    
//    @Override
//    public List<AnswerDto> findByExamCategory(String categoryTitle) {
//        List<Answer> answers = answerRepo.findByExamCategory(categoryTitle);
//        return answers.stream()
//                      .map(answer -> modelMapper.map(answer, AnswerDto.class))
//                      .collect(Collectors.toList());
//    }
//    


    @Override
    public AnswerDto createAnswer(AnswerDto answerDto, Integer examId, Integer userId) {
        // Fetch the Exam and User entities based on provided IDs
        Exam exam = this.examRepo.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam", "exam id", examId));

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));

        // Map the DTO to the Answer entity
        Answer answer = this.modelMapper.map(answerDto, Answer.class);

        // Set additional properties on the Answer entity
        answer.setExam(exam);
        answer.setAddedDate(LocalDateTime.now());
        answer.setImageName("");
        answer.setUser(user);

        // Save the Answer entity to the repository
        Answer savedAnswer = this.answerRepo.save(answer);
        AnswerDto savedAnswerDto = this.modelMapper.map(savedAnswer, AnswerDto.class);
        
        // Map the saved Answer entity back to DTO
          return savedAnswerDto;
    }


    @Override
    public void deleteAnswer(Integer answerId) {
        Answer answer = this.answerRepo.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "answer id", answerId));
        this.answerRepo.delete(answer);
    }

    @Override
    public AnswerDto updateAnswer(AnswerDto answerDto, Integer answerId) {
        Answer answer = this.answerRepo.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "answer id", answerId));

        answer.setImageName(answerDto.getImageName());

        Answer updatedAnswer = this.answerRepo.save(answer);
        return this.modelMapper.map(updatedAnswer, AnswerDto.class);
    }

    @Override
    public AnswerDto getAnswerById(Integer answerId) {
        Answer answer = this.answerRepo.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", "answer id", answerId));
        return this.modelMapper.map(answer, AnswerDto.class);
    }

//
//    private UserDto convertToUserDto(User user) {
//        return modelMapper.map(user, UserDto.class);
//    }
//    


}
