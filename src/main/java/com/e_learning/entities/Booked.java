package com.e_learning.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@NoArgsConstructor
public class Booked {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookedId;
	
	  @ManyToOne
	    @JoinColumn(name = "category_id")
	    private Category category;

	    @ManyToOne
	    private User user;
}
