package com.e_learning.services;

import java.util.List;

import com.e_learning.payloads.LiveStreamingDto;
import com.e_learning.payloads.PostDto;
import com.e_learning.payloads.UserDto;


public interface LiveStreamingService {

	//create 

			LiveStreamingDto createLiveStreaming(LiveStreamingDto liveDto,Integer userId,Integer categoryId);

			
			//get all lives by category
			
			List<LiveStreamingDto> getLiveStreamingByCategory(Integer categoryId);
			
			List<LiveStreamingDto>getLiveStreamingsByUserFaculty(Integer userId);
			
			List<LiveStreamingDto> getAllLives();
}
