package com.ecom.controller;


import com.ecom.entity.Category;
import com.ecom.entity.Product;
import com.ecom.entity.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/Admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userservice;
	
	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if(p!=null) {
			String email = p.getName();
			UserDtls byEmail = userservice.getUserByEmail(email);
			m.addAttribute("user", byEmail);
			
		}
		List<Category>activeCategory=categoryService.getAllActiveCategory();
		m.addAttribute("categorys", activeCategory);
	} 

	@GetMapping("/")
	public String index() {
		return"admin/index";
	}
	@GetMapping("/addproduct")
	public String addproduct(Model m) {
		List<Category> categories = categoryService.getCategory();
		m.addAttribute("categories", categories);
		
		
		return"admin/addproduct";
	}
	@GetMapping("/category")
	public String category(Model m) {
		m.addAttribute("categorys",categoryService.getCategory());
		return"admin/category";
	}
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session ) throws IOException {

		String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
		category.setImageName(imageName);

		if( categoryService.saveCategory(category)!=null ) {
			session.setAttribute("successMsg", "Successfully saved category");

		}


			File saveFile = new ClassPathResource("static/img").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
					+ file.getOriginalFilename());
			System.out.println(path);
			Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);


		return"redirect:/Admin/category";
	}
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable Long id, HttpSession session){
		boolean b = categoryService.deleteCategory(id);
			System.out.println(b);
		if(b) {
            session.setAttribute("successMsg", "Successfully deleted category");
        }else {
            session.setAttribute("errorMsg", "Error in deleting category");
        }

		return"redirect:/Admin/category";
	}
	
	@GetMapping("/editCategory/{id}")
	public String editCategory(@PathVariable Long id,Model m) {
		System.out.println(id);
		
		 m.addAttribute("category", categoryService.getcategory(id));
		 
		
		return"/Admin/edit_category";
	}
	
	
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category,@RequestParam ("file") MultipartFile file,HttpSession Session) throws IOException {
		
		Category oldcategory = categoryService.getcategory(category.getId());
		if(!ObjectUtils.isEmpty(category)) {
			oldcategory.setName(category.getName());
			oldcategory.setIsActive(category.getIsActive());
			String imageName = file.isEmpty() ? oldcategory.getImageName():file.getOriginalFilename();
			oldcategory.setImageName(imageName);
		}
		Category updateCategory = categoryService.saveCategory(oldcategory);
		if(!file.isEmpty()) {
			File saveFile = new ClassPathResource("static/img").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
					+ file.getOriginalFilename());
			System.out.println(path);
			Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			
		}
		
		if( ! ObjectUtils.isEmpty(updateCategory)) {
			Session.setAttribute("successMsg", "category updated successfully");
				
		}else {
			Session.setAttribute("errorMsg", "data wrong inserted successfully");
			
		}
		
		
		return"redirect:/Admin/editCategory/"+category.getId();
	}
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product,@RequestParam ("file") MultipartFile file, HttpSession session) throws Exception {
		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		product.setImage(imageName);
		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());
		Product saveProduct = productService.saveProduct(product);
		
		if(!ObjectUtils.isEmpty(saveProduct)){
			File saveFile = new ClassPathResource("static/img").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
					+ file.getOriginalFilename());
			Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING); 	
			session.setAttribute( "successMsg", "Product saved successfully");
		}else{
			session.setAttribute("errorMsg", "Product not saved");
		}

		return "redirect:/Admin/addproduct";
	}
	@GetMapping("/products")
	public String loadviewproject( Model m) {
		m.addAttribute("products", productService.getAllProducts());
		
		
		return"/Admin/product";
	}
	
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct( @PathVariable Long id, HttpSession session) {
		boolean deleteProduct = productService.deleteProduct(id);
		
		if(deleteProduct) {
			session.setAttribute("successMsg","product deleted successfully");
		}else {
			session.setAttribute("errorMsg","product not deleted ");

		}
		
		
		
		return"redirect:/Admin/products";
	}
	
	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable Long id,Model m) {
		
		 m.addAttribute("product", productService.getProductById(id));
		 m.addAttribute("categories", categoryService.getCategory());
		 
		
		return"admin/editProduct";
	}
	
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product,HttpSession session,@RequestParam("file")MultipartFile file) throws IOException {
		if(product.getDiscount()<0 || product.getDiscount()>100) {
			
			session.setAttribute("errorMsg", "invalid discount ");
		}else {
		
		
		 Product updateProduct = productService.updateProduct(product, file);
		 if(!ObjectUtils.isEmpty(updateProduct)) {
			 session.setAttribute("successMsg", "product updated successfully"); 
		 }else {
			 session.setAttribute("errorMsg", "category not updated ");
		 }
		}
		
		return"redirect:/Admin/editProduct/" +product.getId();
	}
	
	@GetMapping("/users")
	public String getAllUsers(Model m) {
		List<UserDtls> users = userservice.getAllUser("ROLE_USER");
		m.addAttribute("users", users);
		
		
		return "/admin/users";
	}
	@GetMapping("/updateStatus")
	public String updateUser(@RequestParam boolean status , @RequestParam long id,HttpSession session) {
		boolean f= userservice.updateAccountStatus(id,status);
		if(f) {
			session.setAttribute("successMsg", "Your account upadeted successfully");
			
		}else {
			session.setAttribute("errorMsg", "Your account not upadeted ");

		}
		
		
		
		return"redirect:/Admin/users";
	}
	
	
	
	
	
	
}

