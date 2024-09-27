package com.lcwd.electronicstore.services;

import java.io.IOException;
import java.util.List;

import com.lcwd.electronicstore.dto.CategoryDto;
import com.lcwd.electronicstore.dto.PageableResponse;

public interface CategoryService {
	CategoryDto createCategory(CategoryDto categoryDto);
	CategoryDto updateCategory(CategoryDto categoryDto,String categoryid);
	void deleteCategory(String categoryId) throws IOException;
	PageableResponse<CategoryDto> getAllCategoryList(int pageNumber,int pageSize,String sortBy,String sorDir);
	CategoryDto getCategoryById(String categoryId);
}
