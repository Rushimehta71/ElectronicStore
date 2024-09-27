package com.lcwd.electronicstore.services.impl;
import java.awt.print.Pageable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.dto.ProductDto;
import com.lcwd.electronicstore.entities.Category;
import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.exceptions.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.HelperUtility;
import com.lcwd.electronicstore.repositories.CategoryRepository;
import com.lcwd.electronicstore.repositories.ProductRepository;
import com.lcwd.electronicstore.services.ProductServices;

import jakarta.persistence.FetchType;

@Service
public class ProductServiceImpl implements ProductServices{


	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ModelMapper modelMapper;

	@Value("${products.profile.image.path}")
	private String imagePath;

	Logger logger=LoggerFactory.getLogger(ProductServiceImpl.class);
	@Override
	public ProductDto createProduct(ProductDto productDto) {
		Product product=modelMapper.map(productDto, Product.class);
		String Id= UUID.randomUUID().toString();
		product.setProductId(Id);
		Date date =new Date();
		//import java.util.date
		product.setAddedDate(date);
		return 	modelMapper.map(productRepository.save(product),ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, String productId) {
		Product product =productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not founf in Database !!"));
		product.setDescription(productDto.getDescription());
		product.setLive(productDto.isLive());
		product.setPriceDiscounted(productDto.getPriceDiscounted());
		product.setPriceOriginal(productDto.getPriceOriginal());
		product.setQuantity(productDto.getQuantity());
		product.setStock(productDto.isStock());
		product.setTitle(productDto.getTitle());
		product.setImageNameString(productDto.getImageNameString());
		return modelMapper.map(productRepository.save(product),ProductDto.class);
	}

	@Override
	public void deleteProduct(String productId) throws IOException {
		Product product =productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not founf in Database !!"));
		//Delete assosiated image
		String fullPath=imagePath+product.getImageNameString();
		Path path = Paths.get(fullPath);
		try {
			Files.delete(path);
		} catch (NoSuchFileException e) {
			logger.info("User Image Not found in folder");
			e.printStackTrace();
		}
		productRepository.delete(product);
	}

	@Override
	public ProductDto getProductById(String productId) {
		Product product =productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not founf in Database !!"));
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		PageRequest pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page= productRepository.findAll(pageable);
		PageableResponse<ProductDto> pageableResponse= HelperUtility.getPageableResponse(page, ProductDto.class);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		PageRequest pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page= productRepository.findByLiveTrue(pageable);
		return HelperUtility.getPageableResponse(page,ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		PageRequest pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page= productRepository.findByTitleContaining(subTitle,pageable);
		return HelperUtility.getPageableResponse(page,ProductDto.class);
	}
	//Inside category controller
	@Override
	public ProductDto createProductWithcategory(ProductDto productDto, String categoryId) {
		//FetchType category
		Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Product not founf in Database !!"));
		System.out.println("Rushikesh cat1"+category.getCategiryId()+" and "+category.getTitle());
		Product product=modelMapper.map(productDto, Product.class);
		String Id= UUID.randomUUID().toString();
		product.setProductId(Id);
		product.setCategory(category);
		Date date =new Date();
		//import java.util.date
		product.setAddedDate(date);
		return 	modelMapper.map(productRepository.save(product),ProductDto.class);
	}
	//Inside category controller
	@Override
	public ProductDto updateProductWithcategory(String categoryId,String productId) {
		//FetchType category
		Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category not founf in Database !!"));
		Product product =productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not founde in the Database !!"));
		product.setCategory(category);
		return 	modelMapper.map(productRepository.save(product),ProductDto.class);
	}

	//Inside category controller
	@Override
	public PageableResponse<ProductDto>  getProductOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir){
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		PageRequest pageable=PageRequest.of(pageNumber, pageSize,sort);
		Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Product not founf in Database !!"));
		Page<Product> productPage =productRepository.findByCategory(category,pageable);
		return HelperUtility.getPageableResponse(productPage,ProductDto.class);
	}

}
