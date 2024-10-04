package com.e_learning.entities;


import java.time.LocalDateTime;


import javax.persistence.*;



import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = "post_title", length = 100, nullable = false)
    private String title;

    @Column(length = 1000000000)
    private String content;

    private String imageName;
    
    private String videoLink;

    private LocalDateTime addedDate;
    
    private String mentor;
    
    //private String price;
    
    //private String discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    private User user;
    
   
}
