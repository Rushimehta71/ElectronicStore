package com.lcwd.electronicstore.dto;

import java.sql.Date;

import com.lcwd.electronicstore.entities.Category;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {
	
	private String productId;
	private String title;

	@Column(length = 1000)
	private String description;

	private int priceOriginal;

	private int priceDiscounted;

	private int quantity;

	private Date addedDate;

	private boolean live;

	private boolean stock;
	
	private String imageNameString; 
	
	//Use Cat DTO other wise it will not parse and use variable name exact which you are useing in product class
	 private CategoryDto category;
}

