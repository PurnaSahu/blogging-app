package com.pbs.blog.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbs.blog.exceptions.*;
import com.pbs.blog.entities.User;
import com.pbs.blog.payloads.UserDto;
import com.pbs.blog.repositories.UserRepository;
import com.pbs.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepo;
	
	/*the problem here will be, our user entity related to our respective entityRepository, so we can pass only pass our entity object, 
	 hence everytime we pass data to DB or get data from DB(will use entity object only) we will convert our data from dto object to our entity object
	 */ 
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
		//create user object from DTO
		User user = this.dtoToUser(userDto);
		
		//we will pass user object to our user repo to pass data to DB
		User savedUser = this.userRepo.save(user);
		
		//but we will not directly pass user object, we will convert and pass. its like UserDto object only will be publicly available.
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		
		/************fetch the user with specific UserID from DB*************/
		//as we are handling the exception if user not found, so here the return type need not to be Object<User>, we can directly keep it in User
		/*usually, the return type is Object<Type> when there is chance of getting NULL data, but as we are handling the Exception when NULL 
		    data comes so nwe can directly hold as User type.
		 */
		
		User userFound = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User"," id ",userId));
		
		/*****************now will update the user we found*****************/
		userFound.setName(userDto.getName());
		userFound.setEmail(userDto.getEmail());
		userFound.setPassword(userDto.getPassword());
		userFound.setAbout(userDto.getAbout());
		
		/**************save the updated object back to database*************/
		User updatedUser = this.userRepo.save(userFound);
		
		/******************pass the user info to its respective DTO and return it**************/
		UserDto userToDto = userToDto(updatedUser);
		
		return userToDto;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		
		/*******************find the user by UserId provided****************/
		User userById = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		
		/*****************convert the data B4 return***************/
		return userToDto(userById);
	}

	@Override
	public List<UserDto> getAllUsers() {
		
		List<User> allUsers = this.userRepo.findAll();
		
		/*******************convert all users to dtos*****************/
		// we can either use for Each or stream to iterate one by one user and covert each user object to its respective DTO
		List<UserDto> userDtos = 
				allUsers.
				stream().
				map(user -> this.userToDto(user)).
				collect(Collectors.toList());
		
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		/******************B4 deleting, we will check existence of user*****************/
		User userExist = this.userRepo.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
		this.userRepo.delete(userExist);
	}

	public User dtoToUser(UserDto userDto) {
		
		User user = this.modelMapper.map(userDto, User.class);
		/*
	         User user = new User();
		user.setId(userDto.getId());
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		*/
		return user;
	}
	
	public UserDto userToDto(User user) {
		UserDto dto = this.modelMapper.map(user, UserDto.class);
	/*
		UserDto dto= new UserDto();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setAbout(user.getAbout());
	*/	
		return dto;
	}
}
