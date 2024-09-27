package com.lcwd.electronicstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronicstore.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

}
