package com.pbs.blog;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogApplicationApisApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplicationApisApplication.class, args);
	}

	/*I want my spring container to generate an object of this, so i can use it in my project workspace, to convert an object to
	 * another type of object
	 * 
	 * Now I will Autowire this Bean in my Service classess to use*/
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
