package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import com.ecom.entity.Category;
import com.ecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.entity.UserDtls;
import com.ecom.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userservice;

	@Autowired
	private CategoryService categoryService;
	


	
	@GetMapping("/")
	public String home() {
		
		
		return "user/homePage";
	}
	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if(p != null) {
			String email = p.getName();
			UserDtls byEmail = userservice.getUserByEmail(email);
			m.addAttribute("user", byEmail);
			
		}
		List<Category> activeCategory=categoryService.getAllActiveCategory();
		m.addAttribute("categorys", activeCategory);
	
	
}
}
