package com.pbs.blog.services;

import java.util.List;

import com.pbs.blog.payloads.CategoryDto;

public interface CategoryService {

	// create Category
	CategoryDto createCategory(CategoryDto categoryDto);
	
	//update Category - takes updated categoryInfo and categoryId
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	
	//delete a category
	void deleteCategory(Integer categoryId);
	
	//get a category Info
	CategoryDto getCategory(Integer categoryId);
	
	//get all categories
	List<CategoryDto> getAllCategories();
}
