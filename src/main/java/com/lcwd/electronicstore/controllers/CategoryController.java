package com.lcwd.electronicstore.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
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

import com.lcwd.electronicstore.dto.ApiResponseMessage;
import com.lcwd.electronicstore.dto.CategoryDto;
import com.lcwd.electronicstore.dto.ImageResponse;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.dto.ProductDto;
import com.lcwd.electronicstore.dto.UserDto;
import com.lcwd.electronicstore.services.CategoryService;
import com.lcwd.electronicstore.services.FileService;
import com.lcwd.electronicstore.services.ProductServices;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	@Autowired
	FileService fileService;

	@Autowired
	ProductServices productServices;

	@Value("${categories.profile.image.path}")
	private String imageUploadPath;

	Logger logger=LoggerFactory.getLogger(CategoryController.class);
	//create
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
		return new ResponseEntity<>(categoryService.createCategory(categoryDto),HttpStatus.CREATED);
	}
	//update
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
			@PathVariable String categoryId){
		return new ResponseEntity<>(categoryService.updateCategory(categoryDto,categoryId),HttpStatus.OK);
	}

	//getSingle
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId){
		return new ResponseEntity<>(categoryService.getCategoryById(categoryId),HttpStatus.OK);
	}

	//getall
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumeber,
			@RequestParam(value="pageSize",defaultValue = "2",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
		return new ResponseEntity<>(categoryService.getAllCategoryList(pageNumeber,pageSize,sortBy,sortDir),HttpStatus.OK);
	}
	//delete
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) throws IOException{
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(ApiResponseMessage.builder()
				.message("Category with ID: "+categoryId+ " got deleted").build(),HttpStatus.OK);
	}

	@PostMapping("/image/{categoryId}")
	//upload category image
	public ResponseEntity<ImageResponse> uploadImage(@RequestParam("categoryImage") MultipartFile image,
			@PathVariable String categoryId) throws IOException{
		String imageName =fileService.uploadImage(image, imageUploadPath);
		CategoryDto category =categoryService.getCategoryById(categoryId);
		category.setCoverImage(imageName);
		categoryService.updateCategory(category,categoryId);
		ImageResponse imageResponse=ImageResponse.builder()
				.imageName(imageName).success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

	}

	@GetMapping("/image/{categoryId}")
	//serve category image
	public void serveUserImage(@PathVariable("categoryId") String categoryId,
			HttpServletResponse response) throws IOException {
		CategoryDto category =categoryService.getCategoryById(categoryId);
		logger.info("User image name :  {}",category.getCoverImage());
		InputStream resource =fileService.getResource(imageUploadPath, category.getCoverImage());
		response.setContentType(MediaType.IMAGE_PNG_VALUE);
		StreamUtils.copy(resource,response.getOutputStream());
	}

	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> createProductWithCategory(
			@PathVariable("categoryId") String categoryId,
			@RequestBody ProductDto productDto){
		ProductDto productWithCategoryDto =productServices.createProductWithcategory(productDto, categoryId);
		return new ResponseEntity<>(productWithCategoryDto,HttpStatus.OK);

	}

	//http://localhost:8080/categories/c8af73b2-f6ea-4714-9683-4ebd41ca49c4/products/c79d09d3-6d2d-454c-bc1a-423fae38d898
	@PutMapping("/{categoryId}/products/{productId}")
	public ResponseEntity<ProductDto> updateProductWithCategory(
			@PathVariable("categoryId") String categoryId,
			@PathVariable("productId") String productId){
		ProductDto productWithCategoryDto =productServices.updateProductWithcategory(categoryId, productId);
		return new ResponseEntity<>(productWithCategoryDto,HttpStatus.OK);

	}

	//http://localhost:8080/categories/c8af73b2-f6ea-4714-9683-4ebd41ca49c4/products
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<PageableResponse<ProductDto>> updateProductWithCategory(
			@PathVariable("categoryId") String categoryId,
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumeber,
			@RequestParam(value="pageSize",defaultValue = "5",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
		PageableResponse<ProductDto> productWithCategoryDto =productServices.getProductOfCategory(categoryId,pageNumeber,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(productWithCategoryDto,HttpStatus.OK);

	}
}
