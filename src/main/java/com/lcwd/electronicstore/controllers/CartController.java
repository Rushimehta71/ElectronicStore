package com.lcwd.electronicstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronicstore.dto.AddItemToCart;
import com.lcwd.electronicstore.dto.ApiResponseMessage;
import com.lcwd.electronicstore.dto.CartDto;
import com.lcwd.electronicstore.services.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carts")
@Tag(name = "Cart Controller",description = "These are Cart APIs")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
    @PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,@RequestBody AddItemToCart request){
		CartDto cartDto = cartService.addIteamToCart(userId, request);
		return new ResponseEntity<CartDto>(cartDto,HttpStatus.OK);
	}
    
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable int itemId,@PathVariable String userId ){
    	cartService.removeItemFromCart(userId, itemId);
    	ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
    	.message("Item is removed from cart")
    	.success(true)
    	.status(HttpStatus.OK)
    	.build();
    	return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
    }
    
    //Clear cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId ){
    	cartService.clearCart(userId);
    	ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
    	.message("cart is clear")
    	.success(true)
    	.status(HttpStatus.OK)
    	.build();
    	return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
    }
    
    //get cart
    @GetMapping("/{userId}")
   	public ResponseEntity<CartDto> getCart(@PathVariable String userId){
   		CartDto cartDto = cartService.getCartByUser(userId);
   		return new ResponseEntity<>(cartDto,HttpStatus.OK);
   	}
}
