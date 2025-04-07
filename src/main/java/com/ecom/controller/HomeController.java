package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.entity.Category;
import com.ecom.entity.Product;
import com.ecom.entity.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userservice;

	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			UserDtls byEmail = userservice.getUserByEmail(email);
			m.addAttribute("user", byEmail);

		}
		List<Category>activeCategory=categoryService.getAllActiveCategory();
		m.addAttribute("categorys", activeCategory);

	}

	@GetMapping("/")
	public String first() {
		return "index";
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/product")
	public String product(Model m, @RequestParam(value = "category", defaultValue = "") String category) {

		List<Category> categories = categoryService.getAllActiveCategory();

		List<Product> products = productService.getAllActiveProducts(category);
		m.addAttribute("category", categories);
		m.addAttribute("products", products);

		return "product";
	}

	@GetMapping("/viewproduct/{id}")
	public String viewproduct(@PathVariable Long id, Model m) {
		Product productById = productService.getProductById(id);
		m.addAttribute("product", productById);
		return "view_product";
	}

	@PostMapping("/saveUser")
	public String saveUSer(@ModelAttribute UserDtls userdetails
			, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {

		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		userdetails.setImage(imageName);
		UserDtls saveUser = userservice.saveUser(userdetails);
		if (!ObjectUtils.isEmpty(saveUser)) {
			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
						+ file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}
			session.setAttribute("successMsg", "Registration successfully");

		} else {
			session.setAttribute("errorMsg", "Something wrong error");

		}

		return "redirect:/register";
	}
	// Forgot password implementation
	
	@GetMapping("/ForgotPass")
	public String showForgotPassword() {
	
		
		return "forgot_password";
	}
	@PostMapping("/ForgotPass")
	public String processForgotPassword(@RequestParam String email, HttpSession session,HttpServletRequest request ) {
		UserDtls userDtls = userservice.getUserByEmail(email);
		if(ObjectUtils.isEmpty(userDtls)) {
			session.setAttribute("errorMsg", "Invalid email");
		}else {
			String resetToken = UUID.randomUUID().toString();
			userservice.udateUserResetToken(email,resetToken);
			
			//generate url
			String url=CommonUtil.GenerateUrl(request)+"/ResetPass?token="+resetToken;
			
			Boolean sendMail = CommonUtil.sendMail();
			if(sendMail) {
				session.setAttribute("successMsg", "Please check your email.. Password reset link is sent");

			}else {
				session.setAttribute("errorMsg", "something went wrong");
	
			}
		}
	
		
		return "redirect:/ForgotPass";
	}

	@GetMapping("/ResetPass")
	public String showresetPassword() {
	
		
		return "reset_pass";
	}

}
	
	



