package com.lcwd.electronicstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronicstore.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
