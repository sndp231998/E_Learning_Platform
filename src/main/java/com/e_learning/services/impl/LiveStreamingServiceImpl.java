package com.e_learning.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e_learning.entities.Category;
import com.e_learning.entities.LiveStreaming;
import com.e_learning.entities.User;
import com.e_learning.exceptions.ResourceNotFoundException;
import com.e_learning.payloads.LiveStreamingDto;
import com.e_learning.repositories.CategoryRepo;
import com.e_learning.repositories.LiveStreamingRepo;
import com.e_learning.repositories.UserRepo;
import com.e_learning.services.LiveStreamingService;

@Service
public class LiveStreamingServiceImpl implements LiveStreamingService {

    @Autowired
    private LiveStreamingRepo liveRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public LiveStreamingDto createLiveStreaming(LiveStreamingDto liveDto, Integer userId, Integer categoryId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

        LiveStreaming live = this.modelMapper.map(liveDto, LiveStreaming.class);
        live.setTitle(liveDto.getTitle());
        live.setStartingTime(liveDto.getStartingTime());
        live.setStreamlink(liveDto.getStreamlink());
        live.setUser(user);
        live.setCategory(category);

        LiveStreaming newLive = this.liveRepo.save(live);

        return this.modelMapper.map(newLive, LiveStreamingDto.class);
    }

    @Override
    public List<LiveStreamingDto> getLiveStreamingByCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

        List<LiveStreaming> lives = this.liveRepo.findByCategory(category);

        List<LiveStreamingDto> liveDtos = lives.stream()
                .map(live -> this.modelMapper.map(live, LiveStreamingDto.class))
                .collect(Collectors.toList());

        return liveDtos;
    }

    @Override
    public List<LiveStreamingDto> getLiveStreamingsByUserFaculty(Integer userId) {
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
        // Fetch live streams associated with the category
        List<LiveStreaming> lives = this.liveRepo.findByCategory(category);

        // Convert live streams to LiveStreamingDto
        List<LiveStreamingDto> liveDtos = lives.stream()
                .map(live -> this.modelMapper.map(live, LiveStreamingDto.class))
                .collect(Collectors.toList());

        return liveDtos;
    }
}
