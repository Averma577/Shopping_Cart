package com.ecom.service;

import com.ecom.entity.Category;

import java.util.List;

public interface CategoryService {
    public Category saveCategory(Category category);
    
    public boolean existCategory(String name);

    public List<Category> getCategory();

    public boolean deleteCategory(Long id);

    public Category getcategory(Long id);
    
    public List<Category>getAllActiveCategory();


}
