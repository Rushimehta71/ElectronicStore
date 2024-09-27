package com.lcwd.electronicstore.controllers;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronicstore.dto.ApiResponseMessage;
import com.lcwd.electronicstore.dto.CreateOrderRequest;
import com.lcwd.electronicstore.dto.OrderDto;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.services.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	//create for user
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
		OrderDto order =orderService.createOrder(request);
		return new ResponseEntity<>(order,HttpStatus.CREATED);
	}


	//Remove order
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removOrder(@PathVariable("orderId") String orderId){
		orderService.removeOrder(orderId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.message("orders is removed")
				.status(HttpStatus.OK)
				.success(true)
				.build();
		return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK );
	}

	//for user
	@GetMapping("/user/{userId}")
	//Get order of the user
	public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
		List<OrderDto> orderList =orderService.getOrderOfUser(userId);
		return new ResponseEntity<>(orderList,HttpStatus.OK);

	}

	@GetMapping
	//Get order of all user it is for Admin 
	public ResponseEntity<PageableResponse<OrderDto>> getOrders(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumeber,
			@RequestParam(value="pageSize",defaultValue = "20",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "billingName",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir){
		PageableResponse<OrderDto> orderList =orderService.getOrders(pageNumeber,pageSize,sortBy,sortDir);
		return new ResponseEntity<>(orderList,HttpStatus.OK);
	}
}
