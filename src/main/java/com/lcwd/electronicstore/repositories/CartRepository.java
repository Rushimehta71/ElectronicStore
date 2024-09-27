package com.lcwd.electronicstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.User;

public interface CartRepository extends JpaRepository<Cart, String> {
Optional<Cart> findByUser(User user);
}
