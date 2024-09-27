package com.lcwd.electronicstore.services;

import java.awt.print.Pageable;
import java.util.List;

import org.apache.coyote.BadRequestException;

import com.lcwd.electronicstore.dto.CreateOrderRequest;
import com.lcwd.electronicstore.dto.OrderDto;
import com.lcwd.electronicstore.dto.PageableResponse;

public interface OrderService {
	
	//create order
	OrderDto createOrder(CreateOrderRequest createOrderRequest) ;
	
	//order  remove
	void removeOrder(String orderId);

	//get order of user
	List<OrderDto> getOrderOfUser(String userId);
	
	//get order
	PageableResponse<OrderDto> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	//other logics related to order

}
