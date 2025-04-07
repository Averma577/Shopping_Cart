package com.ecom.util;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {
	
	public static Boolean sendMail() {
		
		return false;
	}

	public static String GenerateUrl(HttpServletRequest request) {
			String Url = request.getRequestURL().toString();
			return Url.replace(request.getServletPath(), "");
			
	}

}
