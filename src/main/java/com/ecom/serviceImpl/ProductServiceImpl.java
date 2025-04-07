package com.ecom.serviceImpl;

import com.ecom.entity.Category;
import com.ecom.entity.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product saveProduct(Product product) {
        // Implement saving product logic here
        return productRepository.save(product);
    }


	@Override
	public List<Product> getAllProducts() {
	return productRepository.findAll();
	
	}


	@Override
	public boolean deleteProduct(Long id) {
		Product product = productRepository.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(product)) {
			productRepository.delete(product);
			return true;
		}
		return false;
		
	}


	@Override
	public Product getProductById(Long id) {
		Product product = productRepository.findById(id).orElse(null);
		return product;
	}


	@Override
	public Product updateProduct(Product product, MultipartFile file) throws IOException {
	    Product dbproduct = getProductById(product.getId());
	    String image = file.isEmpty() ? dbproduct.getImage() : file.getOriginalFilename();
	    dbproduct.setImage(image);
	    dbproduct.setTitle(product.getTitle());
	    dbproduct.setDescription(product.getDescription());
	    dbproduct.setCategory(product.getCategory());
	    dbproduct.setPrice(product.getPrice());
	    dbproduct.setIsActive(product.isActive());
	    dbproduct.setStock(product.getStock());
	    dbproduct.setDiscount(product.getDiscount());

	    // Change to percentage
	    double discount = product.getPrice() * (product.getDiscount() / 100.0);
	    double discountPrice = product.getPrice() - discount;
	    dbproduct.setDiscountPrice(discountPrice);

	    Product savedProduct = productRepository.save(dbproduct);

	    if (!ObjectUtils.isEmpty(savedProduct)) {
	        if (!image.isEmpty()) {
	            try {
	                File saveFile = new ClassPathResource("static/img").getFile();
	                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
	                        + file.getOriginalFilename());
	                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	                System.out.println("Error saving image: " + e.getMessage());
	            }
	        }
	        return savedProduct;
	    }

	    return null;
	}


	@Override
	public List<Product> getAllActiveProducts(String category) {
		
		List<Product> byIsActiveTrue = null;
		if(ObjectUtils.isEmpty(category)) {
			byIsActiveTrue = productRepository.findByIsActiveTrue();
		}
		else {
			byIsActiveTrue=productRepository.findByCategory(category);
		}
		
		
		
		return byIsActiveTrue;
	}


	
	



	
}
