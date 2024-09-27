package com.lcwd.electronicstore.services;

import java.io.IOException;
import java.util.List;

import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.dto.ProductDto;

public interface ProductServices {
	//Create
	ProductDto createProduct(ProductDto productDto);

	//update
	ProductDto updateProduct(ProductDto productDto,String productId);

	//delete
	void deleteProduct(String productId) throws IOException;

	//Get single
	ProductDto getProductById(String productId);

	//getAll
	PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy,String sortDir);

	//getAllLive
	PageableResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize,String sortBy,String sortDir);

	//Search Product
	PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);
	
   //Create product with category
	ProductDto createProductWithcategory(ProductDto productdto,String categoryId);

	ProductDto updateProductWithcategory(String categoryId, String productId);

	PageableResponse<ProductDto> getProductOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
	

}
