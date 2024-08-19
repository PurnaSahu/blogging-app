package com.pbs.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbs.blog.entities.Category;
import com.pbs.blog.exceptions.ResourceNotFoundException;
import com.pbs.blog.payloads.CategoryDto;
import com.pbs.blog.repositories.CategoryRepo;
import com.pbs.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private Category dtoToCategory(CategoryDto categoryDto){
		Category category = this.modelMapper.map(categoryDto, Category.class);
		return category;
	}
	
	private CategoryDto categoryToDto(Category category) {
		CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
		return categoryDto;
	}
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		//Category dtoToCategory = this.dtoToCategory(categoryDto); -> i'm directly passing the object
		Category savedCategory= this.categoryRepo.save(this.dtoToCategory(categoryDto));
		return this.categoryToDto(savedCategory);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		
		//1st will handle exception whether the id exists or not?
		Category cat= this.categoryRepo.findById(categoryId).
				orElseThrow( () -> new ResourceNotFoundException("Category", "category Id", categoryId));
		
		// now, the old data will be updated with new data
		cat.setCategoryTitle(categoryDto.getCategoryTitle());
		cat.setCategoryDescription(categoryDto.getCategoryDescription());
		
		//now, save the new data in DB
		Category updatedData = this.categoryRepo.save(cat);
		return this.categoryToDto(updatedData);
	}

	@Override
	public void deleteCategory(Integer categoryId) {

		Category userExist = this.categoryRepo.findById(categoryId).
				orElseThrow( ()-> new ResourceNotFoundException("Category", "Category ID", categoryId));
		
		//below condition checking not at all needed, if the id not present then we already handling the exception
		if(userExist != null) {
			this.categoryRepo.deleteById(categoryId);
		}
	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		Category gotCategory = this.categoryRepo.findById(categoryId).
				orElseThrow( () -> new ResourceNotFoundException("Category ","Category Id ",categoryId));
		return this.categoryToDto(gotCategory);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		
		List<Category> findAllCategories = this.categoryRepo.findAll();
		
		List<CategoryDto> collectedCategoryDtos = findAllCategories.
				stream().
				map( (cat) -> this.categoryToDto(cat)).
				collect(Collectors.toList());
		
		return collectedCategoryDtos;
	}

}
