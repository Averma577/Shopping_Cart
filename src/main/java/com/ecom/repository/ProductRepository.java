package com.ecom.repository;

import com.ecom.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByIsActiveTrue();

	List<Product> findByCategory(String category);

	List<Product> findByCategoryAndIsActiveTrue(String category);
}