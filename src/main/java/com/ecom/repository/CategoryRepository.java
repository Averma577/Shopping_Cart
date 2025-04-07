package com.ecom.repository;

import com.ecom.entity.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	

	boolean findByName(String name);

	List<Category> findByIsActiveTrue();
	
	

	
}