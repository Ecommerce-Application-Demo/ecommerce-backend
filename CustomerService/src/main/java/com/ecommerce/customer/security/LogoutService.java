package com.ecommerce.customer.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogoutService {

	@Autowired
	JwtHelper jwtHelper;
	@Autowired
	StringRedisTemplate redisTemplate;

	public void logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);
		String sessionKey=jwtHelper.extractAllClaims(token).get("sessionKey").toString();
		try {
			redisTemplate.delete(sessionKey);
			SecurityContextHolder.clearContext();
		} catch (NullPointerException e) {
			log.error(e.getMessage(),e);
			SecurityContextHolder.clearContext();
		}
	}

}
