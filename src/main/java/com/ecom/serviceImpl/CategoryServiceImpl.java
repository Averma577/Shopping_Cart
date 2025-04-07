package com.ecom.serviceImpl;

import com.ecom.entity.Category;
import com.ecom.repository.CategoryRepository;
import com.ecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
    	
        return categoryRepository.save(category);

    }

    @Override
    public List<Category> getCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean deleteCategory(Long id) {

        Category category = categoryRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(category )){
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
	public boolean  existCategory(String name) {
        return categoryRepository.findByName(name);

		
	}

	@Override
	public Category getcategory(Long id) {
		 Category category = categoryRepository.findById(id).orElse(null);
		return category;
	}

	@Override
	public List<Category> getAllActiveCategory() {
		return  categoryRepository.findByIsActiveTrue();
		
		
	}
}
