package com.ecom.service;

import com.ecom.entity.Product;
import com.ecom.repository.ProductRepository;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
// TODO: Implement product related operations
    public Product saveProduct(Product product);
    
    public List<Product> getAllProducts();
    
    public boolean deleteProduct(Long id);
    
    public Product getProductById(Long id);
    
    public Product updateProduct(Product product,MultipartFile file) throws IOException;
    
    public List<Product>getAllActiveProducts(String category);
}
