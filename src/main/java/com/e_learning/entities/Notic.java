package com.e_learning.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notics")
@Data
@NoArgsConstructor
public class Notic {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticId;

	@Column(length = 1000000000)
    private String content;
	
	  private LocalDateTime addedDate;
	  
	  private String imageName;
	  
	  
}
