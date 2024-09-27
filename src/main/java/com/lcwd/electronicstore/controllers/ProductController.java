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
import com.lcwd.electronicstore.services.FileService;
import com.lcwd.electronicstore.services.ProductServices;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	ProductServices productServices;
	
	@Autowired
	FileService fileService;
	
	@Value("${products.profile.image.path}")
	private String imageUploadPath;
	
	Logger logger=LoggerFactory.getLogger(ProductController.class);
	
	@PostMapping
	//create product
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
		ProductDto productDtoCreated=productServices.createProduct(productDto);
		return new ResponseEntity<ProductDto>(productDtoCreated,HttpStatus.CREATED);
	}
	
	@PutMapping("/{productId}")
	//Update Product
	public ResponseEntity<ProductDto>  updateProduct(@RequestBody ProductDto productDto,@PathVariable String productId){
		ProductDto productDtoUpdated=productServices.updateProduct(productDto,productId);
		return new ResponseEntity<ProductDto>(productDtoUpdated,HttpStatus.OK);
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) throws IOException{
		productServices.deleteProduct(productId);	
		ApiResponseMessage apiResponseMessage=ApiResponseMessage.builder().message("Product Got Deleted").build();
		return new ResponseEntity<ApiResponseMessage> (apiResponseMessage,HttpStatus.OK);
	}
	
	@GetMapping("/{productId}")
	//create product
	public ResponseEntity<ProductDto> getProduct(@PathVariable String productId){
		ProductDto productDto=productServices.getProductById(productId);
		return new ResponseEntity<ProductDto>(productDto,HttpStatus.OK);
	}
	
	
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumeber,
			@RequestParam(value="pageSize",defaultValue = "20",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
		PageableResponse<ProductDto> productDto=productServices.getAllProduct(pageNumeber,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(productDto,HttpStatus.OK);
	}
	
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumeber,
			@RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
		PageableResponse<ProductDto> productDto=productServices.getAllLiveProduct(pageNumeber,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(productDto,HttpStatus.OK);
	}
	
	//Search
	@GetMapping("/search/{query}")
	public ResponseEntity<PageableResponse<ProductDto>> searchByTitle(
			@PathVariable String query,
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumeber,
			@RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
		PageableResponse<ProductDto> productDto=productServices.searchByTitle(query,pageNumeber,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(productDto,HttpStatus.OK);
	}
	
	
	@PostMapping("/image/{productId}")
    //upload product image
	public ResponseEntity<ImageResponse> uploadImage(@RequestParam("image") MultipartFile image,@PathVariable("productId") String productId) throws IOException{
		String imageName =fileService.uploadImage(image, imageUploadPath);
		logger.info("User image name while post :  {}",imageName);
		ProductDto productDto =productServices.getProductById(productId);
		productDto.setImageNameString(imageName);
		logger.info("User image name while afetr post :  {} {}",imageName, productDto.getImageNameString());
		productServices.updateProduct(productDto,productId);
		ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/image/{productId}")
	//serve product image
	public void serveUserImage(@PathVariable("productId") String productId,HttpServletResponse response) throws IOException {
		ProductDto productDto =productServices.getProductById(productId);
		logger.info("User image name :  {}",productDto.getImageNameString());
		InputStream resource =fileService.getResource(imageUploadPath, productDto.getImageNameString());
		response.setContentType(MediaType.IMAGE_PNG_VALUE);
		StreamUtils.copy(resource,response.getOutputStream());
	}
}
