package com.pbs.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/*Rather then directly accessing User entity classess , we have created another replica of it (Data transfer object) we will gett data to the DTO 
 * classess first and then pass it to our respective entity class, similarly we will pass data from Entity to DTO and from there we will do all 
 * neccesary operation with data
 * make sure the variable names are identical otherwise mapping will cause issues.
 * In short we can not access or manipulate data directly in Entity object*/
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int id;
	
	@Email(message="not a valid mail address !!")
	private String email;
	
	@NotEmpty
	@Size(min=4, message="user name should have minimum 4 charecters !!")
	private String name;
	
	@NotEmpty
	@Size(min=3, max=10, message="password length should be between 3 to 10 !!")
	//@Pattern(regexp = )
	private String password;
	
	@NotNull
	@NotEmpty
	private String about;
}
