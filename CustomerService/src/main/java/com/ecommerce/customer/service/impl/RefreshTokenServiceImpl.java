package com.ecommerce.customer.service.impl;

import java.time.Instant;
import java.util.UUID;
import com.ecommerce.customer.Constants;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.RefreshTokenRepository;
import com.ecommerce.customer.service.declaration.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ecommerce.customer.entity.JwtRefreshToken;

@Service
@Transactional
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
	@Value("#{new Integer(${REFRESH_TOKEN_VALIDITY})}")
	public int REFRESH_TOKEN_VALIDITY;

	@Autowired
    RefreshTokenRepository refreshTokenRepository;

	@Override
	public String getRefreshToken(String emailDto) {
		String email = emailDto;
		JwtRefreshToken newToken = new JwtRefreshToken(email.toLowerCase(), UUID.randomUUID().toString(),
				Instant.now().plusMillis(REFRESH_TOKEN_VALIDITY));
		refreshTokenRepository.save(newToken);
		return newToken.getToken();
	}
	
	public JwtRefreshToken retrieveTokenFromDb(String token) throws CustomerException{
		return refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new CustomerException("TOKEN.NOT.FOUND", HttpStatus.BAD_REQUEST));
	}
	
	@Override
	public Instant extractExpiration(String token) throws CustomerException {
		return retrieveTokenFromDb(token).getExpirationDate();
	}

	@Override
	public String tokenValidation(String token) throws CustomerException {
		JwtRefreshToken refreshtoken=retrieveTokenFromDb(token);
		if (refreshtoken.getExpirationDate().isAfter(Instant.now())) {
			return refreshtoken.getEmail();
		} else {
			throw new CustomerException("TOKEN.EXPIRED", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public void deleteToken(String input) throws CustomerException {
		retrieveTokenFromDb(input);
		refreshTokenRepository.deleteById(input);
	}
	
	@Scheduled(cron = Constants.FIXED_DELAY)
	void cleanup() {
		refreshTokenRepository.findAll().forEach(token -> {
			if (token.getExpirationDate().isBefore(Instant.now())) {
				refreshTokenRepository.delete(token);
				log.info("Refresh Token Repo cleanup executed.");
			}
		});
	}
}
