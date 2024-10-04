package com.e_learning.payloads;



import com.e_learning.entities.Category;
import com.e_learning.entities.User;

import lombok.Data;
@Data
public class BookedDto {
	
    private int bookedId;
	
	
	    private CategoryDto category;

	    private UserDto user;
}
