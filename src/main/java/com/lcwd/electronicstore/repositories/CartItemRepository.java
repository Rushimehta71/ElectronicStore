package com.lcwd.electronicstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronicstore.entities.CartItam;

public interface CartItemRepository extends JpaRepository<CartItam,Integer> {

}
