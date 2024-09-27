package com.lcwd.electronicstore.services;

import com.lcwd.electronicstore.dto.AddItemToCart;
import com.lcwd.electronicstore.dto.CartDto;

public interface CartService {
	//Add itames to cart
	//cart for user is not available  we will create cart 
	//if cart is available add itams to the cart
	
	CartDto addIteamToCart(String userId,AddItemToCart request); 
	void removeItemFromCart(String userId,int cartItem);
	void clearCart(String userId);
	CartDto getCartByUser(String userId);
}
