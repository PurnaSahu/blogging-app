package com.pbs.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pbs.blog.entities.Category;
import com.pbs.blog.entities.Post;
import com.pbs.blog.entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	// find all posts of an user
	//@Query("select p from Post p where p.user.id= :id")
	//List<Post> findByUser(Integer id);
	List<Post> findByUser(User user);
	
	//find all posts by a category
	List<Post> findByCategory(Category category);
	
	//search posts from DB based on keywords recieved for title column
	/*
	 By writting query also we can maintain  this feature
	 
	 @Query("select p from Post where p.title like :key")
	 List<Post> findByTitleContaining(@Param("key") String title);
	 */
	List<Post> findByTitleContaining(String title);
}
