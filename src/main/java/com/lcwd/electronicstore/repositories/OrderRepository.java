package com.lcwd.electronicstore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronicstore.dto.UserDto;
import com.lcwd.electronicstore.entities.Order;
import com.lcwd.electronicstore.entities.User;

public interface OrderRepository extends JpaRepository<Order, String> {
	List<Order> findByUser(User user);
	
}
