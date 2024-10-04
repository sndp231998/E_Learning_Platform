package com.e_learning.payloads;



import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class PostDto {

	private Integer postId;
	
	private String title;
	
	private String content;
	
	private String imageName;
	
	private String videoLink;
	
	private LocalDateTime addedDate;
	
    private String mentor;
    
//    private String price;
//    private String discount;
	
	private CategoryDto category;

	private UserDto user;
	
	


}
