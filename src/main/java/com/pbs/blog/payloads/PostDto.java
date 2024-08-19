package com.pbs.blog.payloads;

import java.util.Date;

import org.hibernate.validator.constraints.UniqueElements;

import com.pbs.blog.entities.Category;
import com.pbs.blog.entities.User;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
//import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class PostDto {

	private Integer postId;
	
	/*these are the only content we need from user rest all data can either store later or we dont need to take from user, 
	 * we can automatically manage
	 * here, UserId and categoryId, either we can pass through RequestBody or as parameter in URL directly
	 */
	//@NotBlank
	//@Size(min=4, max= 30)
	private String title;
	
	//@NotBlank
	private String content;
	
	// we will pass the image later directly to Entity, but here initially a post created we will keep a default image or no need
	private String imageName;
	
	private Date postCreatedDate;
	
	/*the data flow happens here like; PostDto -> Category -> from Category we again found -> Post, to break this flow
	 * we will hold the data in categoryDto
	 * 
	 * coz if i hold the data in CategoryDto then there is No such object with Post which will create a loop same for User*/
	private CategoryDto category;
	
	private UserDto user;
}
