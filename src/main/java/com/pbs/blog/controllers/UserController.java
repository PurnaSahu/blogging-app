package com.pbs.blog.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbs.blog.payloads.ApiResponse;
import com.pbs.blog.payloads.UserDto;
import com.pbs.blog.services.UserService;

import jakarta.validation.Valid;

@RestController
//@Validated
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/****************POST- create/register user data***************/
	//the only purpose we are returning DTO object, coz we dont want to expose directly our entity object
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto createdUserDto = userService.createUser(userDto);
		return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
	}
	
	/***************PUT - Update user data***********/
	//we will take updated user data on requestBody and the id of user through Path URI variable
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer id){
		UserDto updatedUser = userService.updateUser(userDto, id);
		return ResponseEntity.ok(updatedUser);
	}
	
	/*****************DELETE - Delete User******************/
	//@SuppressWarnings("unchecked")
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer userId){
		this.userService.deleteUser(userId);
		//1. return ResponseEntity.ok(Map.of("message","User deleted successfully...")); , if we also want to return status code then
		//2. return new ResponseEntity(Map.of("message","User deleted successfully..."), HttpStatus.OK);
		/*now here, once successfully user deleted, client will get 1 object with 2 property  */
		
		//3rd way, we will create manually a response class to send response
		return new ResponseEntity<ApiResponse>(new ApiResponse("User with ID: "+userId+" deleted successfully...",true), HttpStatus.OK);
	}
	
	/*********************GET -  all Users data******************/
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(this.userService.getAllUsers());
	}
	
	/********************GET - fetch a single user******************/
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId") Integer uid){
		return ResponseEntity.ok(this.userService.getUserById(uid));
	}
}
