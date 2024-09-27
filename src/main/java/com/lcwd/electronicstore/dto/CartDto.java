package com.lcwd.electronicstore.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.lcwd.electronicstore.entities.CartItam;
import com.lcwd.electronicstore.entities.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class CartDto {
	
	private String cartId;

	private Date createAt;
	
	private UserDto user;
	
	private List<CartItamDto> itams =new ArrayList<>();
	
}
