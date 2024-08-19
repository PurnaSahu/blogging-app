package com.pbs.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private Integer categoryId;
	
	@NotBlank
	@Size(min=4, max=100, message="Length of the charecters in Title has to be between 4 to 100 !!")
	private String categoryTitle;
	
	@NotBlank
	@Size(min=10, message="Minimum length of the description field should be 10 !!")
	private String categoryDescription;
}
