package com.lcwd.electronicstore.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.logging.LoggersEndpoint.LoggersDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.lcwd.electronicstore.dto.CategoryDto;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.entities.Category;
import com.lcwd.electronicstore.exceptions.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.HelperUtility;
import com.lcwd.electronicstore.repositories.CategoryRepository;
import com.lcwd.electronicstore.services.CategoryService;

import lombok.experimental.Helper;

@Component
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ModelMapper modelMapper;
	@Value("${categories.profile.image.path}")
	private String imagePath;
	
	Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		String Id= UUID.randomUUID().toString();
		categoryDto.setCategiryId(Id);
		Category category = modelMapper.map(categoryDto, Category.class);
		Category categoryReturn=categoryRepository.save(category);
		CategoryDto categoryDtoReturn = modelMapper.map(categoryReturn, CategoryDto.class);
		return categoryDtoReturn;
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto,String categoryId) {
		//get category of given id
		Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not founf in Database !!"));

		//update category
		category.setTitle(categoryDto.getTitle());
		category.setCoverImage(categoryDto.getCoverImage());
		category.setDescription(categoryDto.getDescription());
		Category updatedCategory = categoryRepository.save(category);
		return modelMapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategoryList(int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		PageRequest pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Category> page= categoryRepository.findAll(pageable);
		PageableResponse<CategoryDto> pageableResponse= HelperUtility.getPageableResponse(page, CategoryDto.class);
		return pageableResponse;
	}

	@Override
	public CategoryDto getCategoryById(String categoryId) {
		//get category of given id
		Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found in Database !!"));
		return  modelMapper.map(category, CategoryDto.class);
		
	}

	@Override
	public void deleteCategory(String categoryId) throws IOException {
		Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category for deleting not found in Database !!"));
		//Delete assosiated image
				String fullPath=imagePath+category.getCoverImage();
				Path path = Paths.get(fullPath);
				try {
					Files.delete(path);
				} catch (NoSuchFileException e) {
				logger.info("User Image Not found in folder");
					e.printStackTrace();
				}
		categoryRepository.delete(category);
	}

}
