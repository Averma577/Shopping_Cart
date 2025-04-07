package com.ecom.serviceImpl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.entity.UserDtls;
import com.ecom.repository.UserDetailsRepository;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDetailsRepository userdetailsRepository;
	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDtls saveUser(UserDtls user) {
		user.setRole("ROLE_USER");
		user.setEnable(true);
		user.setAccountNonLocked(true);
		user.setFailedAttemp(0);
		String encodepassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodepassword);
		UserDtls save = userdetailsRepository.save(user);
		return save;
		
	
	}

	@Override
	public UserDtls getUserByEmail(String email) {
		return userdetailsRepository.findByEmail(email);
	}

	@Override
	public List<UserDtls> getAllUser(String role) {
		
	return userdetailsRepository.findByRole(role);
		
		
	}


	@Override
	public boolean updateAccountStatus(long id, boolean status) {
		 Optional<UserDtls> byId = userdetailsRepository.findById(id);
		 if(byId.isPresent()) {
			 UserDtls userDtls = byId.get();
			 userDtls.setEnable(status);
			 userdetailsRepository.save(userDtls);
			 return true;
		 }
	
		return false;
	}

	@Override
	public void increaseFailedAttempt(UserDtls userDtls) {
		int attempt=userDtls.getFailedAttemp()+1;
		userDtls.setFailedAttemp(attempt);
		userdetailsRepository.save(userDtls);
	}

	@Override
	public void userAccountLock(UserDtls userDtls) {
		userDtls.setAccountNonLocked(false);
       userDtls.setLockTime(new Date(0));
	   userdetailsRepository.save(userDtls);
		
		
	}

	@Override
	public boolean unlockAccountTimeExpired(UserDtls userDtls) {
		long lockTime=userDtls.getLockTime().getTime();
		long unlockTime= lockTime+AppConstant.UNLOCK_DURATION_TIME;
		long timeMillis = System.currentTimeMillis();
		
		if(unlockTime<timeMillis) {
			userDtls.setAccountNonLocked(true);
			userDtls.setFailedAttemp(0);
			userDtls.setLockTime(null);
			userdetailsRepository.save(userDtls);
			return true;
		}

		return false;
	}

	@Override
	public void resetAttempt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void udateUserResetToken(String email, String resetToken) {
		UserDtls byEmail = userdetailsRepository.findByEmail(email);
		byEmail.setReset_token(resetToken);
		userdetailsRepository.save(byEmail);
	}

	

}
