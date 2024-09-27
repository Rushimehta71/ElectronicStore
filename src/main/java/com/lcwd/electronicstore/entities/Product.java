package com.lcwd.electronicstore.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Product")
public class Product {
	
	@Id
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
	
	@ManyToOne
	@JoinColumn(name="linked_with_category")
	private Category category;
	

}
