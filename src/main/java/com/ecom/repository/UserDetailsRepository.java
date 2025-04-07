package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.entity.UserDtls;

public interface UserDetailsRepository extends JpaRepository<UserDtls,Long>{

	public UserDtls findByEmail(String email);

	public List<UserDtls> findByRole(String role);

}
