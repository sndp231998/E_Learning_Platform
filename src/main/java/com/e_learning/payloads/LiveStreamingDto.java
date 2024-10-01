package com.e_learning.payloads;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class LiveStreamingDto {


	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Liveid;
	
	  private String title;
	
	
	private String startingTime;
	
	private String streamlink;
	  
	    private CategoryDto category;

	    
	    private UserDto user;
}
