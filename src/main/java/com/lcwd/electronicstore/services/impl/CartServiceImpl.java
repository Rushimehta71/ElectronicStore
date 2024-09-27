package com.lcwd.electronicstore.services.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronicstore.dto.AddItemToCart;
import com.lcwd.electronicstore.dto.CartDto;
import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.CartItam;
import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exceptions.BadApiRequestException;
import com.lcwd.electronicstore.exceptions.ResourceNotFoundException;
import com.lcwd.electronicstore.repositories.CartItemRepository;
import com.lcwd.electronicstore.repositories.CartRepository;
import com.lcwd.electronicstore.repositories.ProductRepository;
import com.lcwd.electronicstore.services.CartService;
import com.lcwd.electronicstore.repositories.UserRepository;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CartServiceImpl implements CartService {


	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CartItemRepository cartItemRepository;


//	@Override
//	public CartDto addIteamToCart(String userId, AddItemToCart request) {
//		String productId=request.getProductId();
//		int quantity=request.getQuantity();
//
//		if(quantity<=0){
//			throw new BadApiRequestException("Requested quantity not valid");
//		}
//		//fetch  the product
//		Product product =productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException());
//
//		//fetch the user form dp
//		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException());
//		Cart cart =null;
//
//		try {
//			cart  =cartRepository.findByUser(user).get();
//		}catch (NoSuchElementException e) {
//			cart = new Cart();
//			cart.setCartId(UUID.randomUUID().toString());
//			cart.setCreateAt(new Date());
//		}
//		AtomicReference<Boolean> updated= new AtomicReference<>(false);
//
//		//cart operation
//		List<CartItam> items = cart.getItams();
//	    items = items.stream().map(item->{
//			if(item.getProduct().getProductId().equals(productId)){
//				item.setQuantity(quantity);
//				item.setTotalPrice(quantity*product.getPriceDiscounted());
//				updated.set(true);
//			}
//			return item;
//		}).collect(Collectors.toList());
//
//		if(!updated.get()) {
//			CartItam CartItems=CartItam.builder()
//					.quantity(quantity)
//					.totalPrice(quantity*product.getPriceDiscounted())
//					.cart(cart)
//					.product(product)
//					.build();
//			cart.getItams().add(CartItems);
//		}
//		cart.setUser(user);
//		Cart updatedCart = cartRepository.save(cart);
//		return modelMapper.map(updatedCart, CartDto.class);
//	}
//	
	@Override
	public CartDto addIteamToCart(String userId, AddItemToCart request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequestException("Requested quantity is not valid !!");
        }

        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));
        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreateAt(new Date());
        }

        //perform cart operations
        //if cart items already present; then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItam> items = cart.getItams();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPriceDiscounted());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItems);

        //create items
        if (!updated.get()) {
        	CartItam cartItem = CartItam.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPriceDiscounted())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItams().add(cartItem);
        }


        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return modelMapper.map(updatedCart, CartDto.class);
    }

	@Override
	public void removeItemFromCart(String userId, int cartItemId) {
		CartItam cartItem =cartItemRepository.findById(cartItemId).orElseThrow(()->new ResourceNotFoundException());
		cartItemRepository.delete(cartItem);
	}

	@Override
	public void clearCart(String userId) {
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException());
		Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException());
		cart.getItams().clear();
		cartRepository.save(cart);
	}

	@Override
	public CartDto getCartByUser(String userId) {
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException());
		Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException());
		return modelMapper.map(cart, CartDto.class);
	}

}
