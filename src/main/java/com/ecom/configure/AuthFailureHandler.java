package com.ecom.configure;

import com.ecom.entity.UserDtls;
import com.ecom.repository.UserDetailsRepository;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private UserService userService;
 
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) 
            throws IOException, ServletException {
        String email = request.getParameter("username");
        UserDtls userDtls = userDetailsRepository.findByEmail(email);
        
        // Null check for userDtls
        if (userDtls == null) {
            exception = new LockedException("User not found with the provided email.");
            super.setDefaultFailureUrl("/signin?error");
            super.onAuthenticationFailure(request, response, exception);
            return;
        }

        // Check if the user account is enabled
        if (userDtls.getIsEnable()) {
            if (userDtls.isAccountNonLocked()) {
                if (userDtls.getFailedAttemp() < AppConstant.ATTEMPT_TIME) {
                    userService.increaseFailedAttempt(userDtls);
                } else {
                    userService.userAccountLock(userDtls);
                    exception = new LockedException("Your account is locked! Failed 3 attempts.");
                }
            } else {
                if (userService.unlockAccountTimeExpired(userDtls)) {
                    exception = new LockedException("Your account is unlocked! Please try to login.");
                } else {
                    exception = new LockedException("Your account is locked! Please try after some time.");
                }
            }
        } else {
            exception = new LockedException("Your account is not enabled.");
        }

        // Redirect to the failure URL with the error message
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
