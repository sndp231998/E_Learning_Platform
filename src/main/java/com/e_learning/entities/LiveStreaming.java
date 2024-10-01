package com.e_learning.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class LiveStreaming {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Liveid;
	
	  private String title;
	
	
	private String startingTime;
	
	private String streamlink;
	
	   @ManyToOne
	    @JoinColumn(name = "category_id")
	    private Category category;

	    @ManyToOne
	    private User user;
}


