package com.pbs.blog.services;

import java.util.List;

import com.pbs.blog.entities.Post;
import com.pbs.blog.payloads.PostDto;
import com.pbs.blog.payloads.PostResponse;


public interface PostService {

	//create
	PostDto createPost(PostDto postDto, Integer userId,  Integer categoryId);
	
	//update
	PostDto updatePost(PostDto postDto, Integer postId);
	
	//delete
	void deletePost(Integer postId);
	
	//get all posts
	//List<PostDto> getAllPosts(Integer pageNo, Integer pageSize);
	PostResponse getAllPosts(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
	
	//get single Post
	PostDto getPostById(Integer postId);
	
	//get all posts by category
	List<PostDto> getAllPostsByCategory(Integer categoryId);
	
	//get all posts by User
	List<PostDto> getAllPostsByUser(Integer userId);
	
	//search posts by name
	List<PostDto> searchPosts(String keyword);
}
