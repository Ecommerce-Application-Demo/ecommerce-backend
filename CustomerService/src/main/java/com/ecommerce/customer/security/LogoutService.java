package com.ecommerce.customer.security;

import java.util.Date;
import com.ecommerce.customer.Constants;
import com.ecommerce.customer.repository.BlockedJwtRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ecommerce.customer.entity.BlockedJwt;

@Service
@Slf4j
public class LogoutService {

	@Autowired
    BlockedJwtRepo blockedJwtRepo;
	@Autowired
	JwtHelper jwtHelper;

	public void logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);
		try {
			BlockedJwt blockedJwt = new BlockedJwt();
			blockedJwt.setJwt(token);
			blockedJwt.setExpirationTime(jwtHelper.extractExpiration(token));
			blockedJwtRepo.save(blockedJwt);
			SecurityContextHolder.clearContext();
		} catch (Exception e) {
			BlockedJwt blockedJwt = new BlockedJwt();
			blockedJwt.setJwt(token);
			blockedJwt.setExpirationTime(new Date());
			blockedJwtRepo.save(blockedJwt);
			SecurityContextHolder.clearContext();
		} 
	}

	@Scheduled(cron = Constants.FIXED_DELAY)
	void cleanup() {
		blockedJwtRepo.findAll().forEach(token -> {
			if (token.getExpirationTime().before(new Date())) {
				blockedJwtRepo.delete(token);
				log.info("Blocked JWT Repo cleanup executed.");
			}
		});
	}
}
