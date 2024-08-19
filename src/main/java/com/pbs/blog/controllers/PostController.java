package com.pbs.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.StreamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pbs.blog.config.AppConstants;
import com.pbs.blog.payloads.ApiResponse;
import com.pbs.blog.payloads.PostDto;
import com.pbs.blog.payloads.PostResponse;
import com.pbs.blog.services.FileService;
import com.pbs.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	//create a post
	@PostMapping("/user/{userId}/category/{categoryId}/post")
	public ResponseEntity<PostDto> createPost(
			@RequestBody PostDto postDto, 
			@PathVariable Integer userId,
			@PathVariable Integer categoryId
			){
		PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto> (createdPost, HttpStatus.CREATED);
	}
	
	//get posts by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser( @PathVariable Integer userId){
		
		List<PostDto> allPostsByUser = postService.getAllPostsByUser(userId);
		
		return new ResponseEntity<List<PostDto>> (allPostsByUser, HttpStatus.OK);
	}
	
	//get posts by category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory( @PathVariable Integer categoryId){
		
		List<PostDto> allPostsByCategory = postService.getAllPostsByCategory(categoryId);
		
		return new ResponseEntity<List<PostDto>> (allPostsByCategory, HttpStatus.OK);
	}
	
	//get All posts
	/*
	  we are going to pass the page number and size as parameter in the link, not as a value
	  
	  if the link dont have these parameters than the default value will be passed for pagination and while we returning pagination data 
	  we are not only sending Info to client. we also sending pageNumber, page size, total elements, totalPages, last page or not and content
	  
	 */
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam( value= "pageNumber", defaultValue =AppConstants.PAGE_NUMBER, required = false) Integer pageNo,
			@RequestParam(value = "recordsInPage",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value= "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value= "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
	{
		
		PostResponse allPosts = this.postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse> (allPosts, HttpStatus.OK);
	}
	
	// get a post by its ID
	@GetMapping("/post/{postId}")
	public ResponseEntity<PostDto> getAllPosts(@PathVariable Integer postId) {
		PostDto post = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(post, HttpStatus.OK);
	}
	
	//delete post
	@DeleteMapping("/post/{postId}")
	public ApiResponse deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ApiResponse("Post with Id: "+postId+" deleted successfully !!", true);
	}
	
	@PutMapping("/post/{postId}")
	public ResponseEntity<PostDto> deletePost(@RequestBody PostDto postDto,@PathVariable Integer postId) {
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}
	
	//search posts
	@GetMapping("/posts/search/{keywords}")
	ResponseEntity<List<PostDto>> postByTitle(
			@PathVariable("keywords") String keywords
			){
		List<PostDto> results = this.postService.searchPosts(keywords);
		
		return new ResponseEntity<List<PostDto>>(results, HttpStatus.OK);
	}
	
	//post Image upload
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable
			Integer postId) throws IOException{
		PostDto postDto = this.postService.getPostById(postId);
		
		String imgName = this.fileService.uploadImage(path, image);
			
		postDto.setImageName(imgName);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}
	
	// method to serve files
	@GetMapping(value="post/image/{imgName}", produces=MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imgName") String imgName, HttpServletResponse response) throws IOException{
		
		InputStream resource = this.fileService.getResource(path, imgName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		org.springframework.util.StreamUtils.copy(resource, response.getOutputStream());
	}
}
