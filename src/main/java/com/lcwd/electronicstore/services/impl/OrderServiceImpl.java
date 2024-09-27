package com.lcwd.electronicstore.services.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.CartItam;
import com.lcwd.electronicstore.entities.Order;
import com.lcwd.electronicstore.entities.OrderItem;
import com.lcwd.electronicstore.dto.CreateOrderRequest;
import com.lcwd.electronicstore.dto.OrderDto;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exceptions.BadApiRequestException;
import com.lcwd.electronicstore.exceptions.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.HelperUtility;
import com.lcwd.electronicstore.repositories.CartRepository;
import com.lcwd.electronicstore.repositories.OrderRepository;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		String userId = orderDto.getUserId();
		String cartId = orderDto.getCartId();
		//fetch user
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
		//fetch cart
		Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with given id not found on server !!"));
		List<CartItam> cartItems = cart.getItams();
		if (cartItems.size() <= 0) {
			throw new BadApiRequestException("Invalid number of items in cart !!");
		}

		//other checks
		Order order = Order.builder()
				.billingName(orderDto.getBillingName())
				.billingPhone(orderDto.getBillingPhone())
				.billingAddress(orderDto.getBillingAddress())
				.orderedDate(new Date())
				.deliveredDate(null)
				.paymentStatus(orderDto.getPaymentStatus())
				.orderStatus(orderDto.getOrderStatus())
				.orderId(UUID.randomUUID().toString())
				.user(user)
				.build();

		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			//	            CartItem->OrderItem
			OrderItem orderItem = OrderItem.builder()
					.quantity(cartItem.getQuantity())
					.product(cartItem.getProduct())
					.totalPrice(cartItem.getQuantity() * cartItem.getProduct().getPriceDiscounted())
					.order(order)
					.build();

			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
			return orderItem;
		}).collect(Collectors.toList());
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());

		cart.getItams().clear();
		cartRepository.save(cart);
		System.out.println("Rushi1  =" +order.getOrderId());
		Order savedOrder = orderRepository.save(order);
		System.out.println("Rushi2  =" +savedOrder.getOrderId());
		OrderDto  orderDto1=  modelMapper.map(savedOrder, OrderDto.class);
		System.out.println("Rushi3  =" +orderDto1.getOrderId());
		return orderDto1;
	}


	@Override
	public void removeOrder(String orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order not found !!"));
		orderRepository.delete(order);
	}

	@Override
	public List<OrderDto> getOrderOfUser(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
		List<Order> orders = orderRepository.findByUser(user);
		List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
		return orderDtos;
	}

	@Override
	public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		PageRequest pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Order> page= orderRepository.findAll(pageable);
		return HelperUtility.getPageableResponse(page, OrderDto.class);
	}

}
