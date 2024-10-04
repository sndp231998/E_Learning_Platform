package com.e_learning.payloads;

import java.time.LocalDateTime;



import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class NoticDto {


    private Integer noticId;

	
    private String content;
	
	  private LocalDateTime addedDate;
	  
	  private String imageName;
}
