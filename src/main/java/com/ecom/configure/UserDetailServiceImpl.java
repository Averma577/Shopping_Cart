package com.ecom.configure;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecom.entity.UserDtls;
import com.ecom.repository.UserDetailsRepository;

public class UserDetailServiceImpl implements UserDetailsService{
		
	@Autowired
	private UserDetailsRepository  userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDtls user = userRepository.findByEmail(username);
		
		if(user==null) {
			throw new  UsernameNotFoundException("User not found");
		}
		return new CustomUser(user);
	}
	
	

}
