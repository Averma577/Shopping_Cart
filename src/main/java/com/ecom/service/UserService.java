package com.ecom.service;

import java.util.List;

import com.ecom.entity.UserDtls;

public interface UserService {

	public UserDtls saveUser(UserDtls user);
	
	public UserDtls getUserByEmail(String email);
	
	
	public List<UserDtls> getAllUser(String role);

	public boolean updateAccountStatus(long id, boolean status);
	
	public void increaseFailedAttempt(UserDtls userDtls);
	
	public void userAccountLock(UserDtls userDtls);
	
	public boolean unlockAccountTimeExpired(UserDtls userDtls);
	
	public void resetAttempt();

	public void udateUserResetToken(String email, String resetToken);
	
	
}
