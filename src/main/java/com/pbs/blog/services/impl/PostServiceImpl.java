package com.pbs.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pbs.blog.entities.Category;
import com.pbs.blog.entities.Post;
import com.pbs.blog.entities.User;
import com.pbs.blog.exceptions.ResourceNotFoundException;
import com.pbs.blog.payloads.PostDto;
import com.pbs.blog.payloads.PostResponse;
import com.pbs.blog.repositories.CategoryRepo;
import com.pbs.blog.repositories.PostRepository;
import com.pbs.blog.repositories.UserRepository;
import com.pbs.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CategoryRepo catRepo;
	
	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		
		// in postDto object we recieves only 2 fields and that will be mapped to Post Object, but rest all fields need to be set by us on Post Entity
		Post newPost = modelMapper.map(postDto, Post.class);
		
		//i will set default photo for the new user
		newPost.setImageName("default.png");
		newPost.setPostCreatedDate(new Date());
		
		User foundUser = userRepo.
						findById(userId).
						orElseThrow(() -> new ResourceNotFoundException("User ","User Id: ",userId));
		Category foundCategory = catRepo.
							findById(categoryId).
							orElseThrow(()-> new ResourceNotFoundException("Category ", "Category Id: ", categoryId));
		newPost.setUser(foundUser);
		newPost.setCategory(foundCategory);
		
		Post savedPost = postRepo.save(newPost);
		
		return this.modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post postToUpdate = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "Post Id:", postId));
		postToUpdate.setTitle(postDto.getTitle());
		postToUpdate.setContent(postDto.getContent());
		postToUpdate.setImageName(postDto.getImageName());
		// at the moment we just want to modify this much thing, later we can add, if we want to modify rest all things
		
		Post updatedPost = this.postRepo.save(postToUpdate);
		
		PostDto mapped = this.modelMapper.map(updatedPost, PostDto.class);
		return mapped;
	}

	@Override
	public void deletePost(Integer postId) {
		Post postToDelete = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "Post Id", postId));
		this.postRepo.delete(postToDelete);
	}

	@Override
	//public List<PostDto> getAllPosts(Integer pageNo, Integer pageSize) {
	public PostResponse getAllPosts(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
		
		Sort sortType=null;
		if (sortDir.equalsIgnoreCase("asc")) {
			sortType= Sort.by(sortBy).ascending();
		}
		else {
			sortType= Sort.by(sortBy).descending();
		}
		
		//int pageSize=2;
		//int pageNo=0; //-> page number by default starts with 0
		//Pageable p= PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Pageable p= PageRequest.of(pageNo, pageSize, sortType);
		/*
		   now, object 'p' will return Page object post 
		 */
		Page<Post> pagePosts = this.postRepo.findAll(p);
		/*
		   pagePosts have all info about pages, like content, page size, number etc..
		 */
		List<Post> foundPosts = pagePosts.getContent();
		
		/*below , without implementing paggination*/
		//List<Post> foundPosts = this.postRepo.findAll();
		
		List<PostDto> converted = foundPosts.
								stream().
								map((post) -> this.modelMapper.map(post, PostDto.class)).
								collect(Collectors.toList());
		
		PostResponse response = new PostResponse();
		response.setContent(converted);
		response.setPageNo(pagePosts.getNumber());
		response.setPageSize(pagePosts.getSize());
		response.setTotalElements(pagePosts.getTotalElements());
		response.setTotalPages(pagePosts.getTotalPages());
		response.setLastPage(pagePosts.isLast());
		
		return response;
	}

	@Override
	public PostDto getPostById(Integer postid) {
		Post postById = this.postRepo.
						findById(postid).
						orElseThrow( () -> new ResourceNotFoundException("Post", "Post Id: ", postid));
		
		return this.modelMapper.map(postById, PostDto.class);
	}

	@Override
	public List<PostDto> getAllPostsByCategory(Integer categoryid) {
		
		Category foundCat = this.catRepo.findById(categoryid).orElseThrow(()-> new ResourceNotFoundException("Category", "Category ID: ", categoryid));
		List<Post> postsByCategory = this.postRepo.findByCategory(foundCat);
		
		List<PostDto> converted = postsByCategory.
								stream().
								map((post) -> this.modelMapper.map(post, PostDto.class)).
								collect(Collectors.toList());
		return converted;
	}

	@Override
	public List<PostDto> getAllPostsByUser(Integer userId) {
		User userFound = this.userRepo.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User", "User Id: ", userId));
		List<Post> postsByUser = postRepo.findByUser(userFound);
		//System.out.println("all posts:"+postsByUser);
		List<PostDto> collect = 
				postsByUser.
				stream().
				map( (post) -> this.modelMapper.map(post, PostDto.class)).
				collect(Collectors.toList());
		System.out.println("all posts after collected:"+collect);
		return collect;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		
		// searching operation will be initiated
		List<Post> titleContaining = this.postRepo.findByTitleContaining(keyword);
		
		List<PostDto> convert = titleContaining.
								stream().
								map( (post) -> this.modelMapper.map(post, PostDto.class)).
								collect(Collectors.toList());
		return convert;
	}

}
