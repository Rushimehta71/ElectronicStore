package com.lcwd.electronicstore.entities;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="cart")
public class Cart {
	
	@Id
	private String cartId;

	private Date createAt;
	
	@OneToOne 
	private User user;
	//fetch = FetchType.EAGER,
	//mapping cart bidirectional mapping
	@OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<CartItam> itams =new ArrayList<>();
	
}
