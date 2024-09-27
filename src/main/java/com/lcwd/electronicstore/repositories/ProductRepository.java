package com.lcwd.electronicstore.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.entities.Category;
import com.lcwd.electronicstore.entities.Product;

public interface ProductRepository extends JpaRepository<Product,String>{
	//Search
	Page<Product> findByTitleContaining(String subTitle,Pageable pageable); 
	Page<Product> findByLiveTrue(Pageable pageable);
	
	//In find by Category method argumnet will be only of Category type
	Page<Product> findByCategory(Category category,Pageable pageable);
	
	//You can write custom finder method or querry mrthod here
}
