package com.ecommerce.customer.service.impl;

import com.ecommerce.customer.Constants;
import com.ecommerce.customer.entity.JwtRefreshToken;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.exception.ErrorCode;
import com.ecommerce.customer.repository.RefreshTokenRepository;
import com.ecommerce.customer.service.declaration.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

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
        JwtRefreshToken newToken = new JwtRefreshToken(emailDto.toLowerCase(), UUID.randomUUID().toString(),
				Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY));
		refreshTokenRepository.save(newToken);
		return newToken.getToken();
	}
	
	public JwtRefreshToken retrieveTokenFromDb(String token) throws CustomerException{
		return refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new CustomerException(ErrorCode.REFRESH_TOKEN_NOT_FOUND.name()));
	}
	
	@Override
	public Instant extractExpiration(String token) throws CustomerException {
		return retrieveTokenFromDb(token).getExpirationDate();
	}

	@Override
	public JwtRefreshToken extendTokenTime(String token) throws CustomerException {
		JwtRefreshToken rt= retrieveTokenFromDb(token);
		rt.setExpirationDate(Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY));
		return refreshTokenRepository.save(rt);
	}

	@Override
	public String tokenValidation(String token) throws CustomerException {
		JwtRefreshToken refreshToken=retrieveTokenFromDb(token);
		if (refreshToken.getExpirationDate().isAfter(Instant.now())) {
			return refreshToken.getEmail();
		} else {
			throw new CustomerException(ErrorCode.REFRESH_TOKEN_EXPIRED.name());
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
