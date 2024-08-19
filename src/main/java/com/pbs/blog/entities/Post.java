package com.pbs.blog.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="post")
@Getter
@Setter
@NoArgsConstructor
@Data
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;
	
	@Column(name="post_title", length=100, nullable=false)
	private String title;
	
	@Column(length=1000)
	private String content;
	
	private String imageName;
	
	private Date postCreatedDate;
	
	/*Now, we will configure our FKs to identify a post belongs to which type of category and belongs to which User*/
	
	//@Column(name="category_id") this way we cant change the column name, coz its not a simple column, its a join column made
	//from joining of with another table column
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@ManyToOne
	private User user;
	
	/*Now, our Post Entity depends on two parent entities (user and category), so if we want to create a post 
	 * then we also need to pass User and Category data */
}
