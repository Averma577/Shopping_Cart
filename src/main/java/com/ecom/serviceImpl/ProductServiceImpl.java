package com.ecom.serviceImpl;

import com.ecom.configure.ProductNotFoundException;
import com.ecom.entity.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public boolean deleteProduct(Long id) {
		Product product = productRepository.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(product)) {
			productRepository.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Product updateProduct(Product product, MultipartFile file) throws IOException {
		Product dbproduct = getProductById(product.getId());
		String image = file.isEmpty() ? dbproduct.getImage() : UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		dbproduct.setImage(image);
		dbproduct.setTitle(product.getTitle());
		dbproduct.setDescription(product.getDescription());
		dbproduct.setCategory(product.getCategory());
		dbproduct.setPrice(product.getPrice());
		dbproduct.setIsActive(product.isActive());
		dbproduct.setStock(product.getStock());
		dbproduct.setDiscount(product.getDiscount());

		double discount = product.getPrice() * (product.getDiscount() / 100.0);
		double discountPrice = product.getPrice() - discount;
		dbproduct.setDiscountPrice(discountPrice);

		Product savedProduct = productRepository.save(dbproduct);

		if (!ObjectUtils.isEmpty(savedProduct)) {
			if (!file.isEmpty()) {
				try {
					File saveFile = new File("uploads" + File.separator + "product_img");
					if (!saveFile.exists()) {
						saveFile.mkdirs(); // Create directory if it doesn't exist
					}
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + image);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					logger.error("Error saving image: " + e.getMessage(), e);
				}
			}
			return savedProduct;
		}

		return null;
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		List<Product> byIsActiveTrue;
		if (category == null || category.isEmpty()) {
			byIsActiveTrue = productRepository.findByIsActiveTrue();
		} else {
			byIsActiveTrue  = productRepository.findByCategoryAndIsActiveTrue(category);
		}
		return byIsActiveTrue;
	}
}
