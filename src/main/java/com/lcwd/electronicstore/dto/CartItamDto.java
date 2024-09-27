package com.lcwd.electronicstore.dto;

import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.Product;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItamDto {
	private int cartItamId;
	
	private ProductDto product;
	
	private int quantity;
	
	private int totalPrice;
}
