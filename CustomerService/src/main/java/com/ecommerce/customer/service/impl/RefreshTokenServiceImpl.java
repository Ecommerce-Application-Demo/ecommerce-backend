package com.ecommerce.customer.service.impl;

import java.time.Instant;
import java.util.UUID;

import com.ecommerce.customer.Constants;
import com.ecommerce.customer.dto.StringInputDto;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.RefreshTokenRepository;
import com.ecommerce.customer.service.declaration.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ecommerce.customer.entity.JwtRefreshToken;
import com.ecommerce.customer.entity.StringInput;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	@Autowired
    RefreshTokenRepository refreshTokenRepository;

	@Override
	public String getRefreshToken(String emailDto) {
		String email = emailDto;
		JwtRefreshToken newToken = new JwtRefreshToken(email, UUID.randomUUID().toString(),
				Instant.now().plusMillis(Constants.REFRESH_TOKEN_VALIDITY));
		refreshTokenRepository.save(newToken);
		return newToken.getToken();
	}

	@Override
	public String tokenValidation(StringInputDto tokenDto) throws CustomerException {
		StringInput token = new StringInput(tokenDto.getInput());
		JwtRefreshToken refreshtoken = refreshTokenRepository.findByToken(token.getInput())
				.orElseThrow(() -> new CustomerException("TOKEN.NOT.FOUND", HttpStatus.BAD_REQUEST));
		if (refreshtoken.getExpirationDate().isAfter(Instant.now())) {
			return refreshtoken.getEmail();
		} else {
			refreshTokenRepository.deleteById(refreshtoken.getEmail());
			throw new CustomerException("TOKEN.INVALID", HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@Scheduled(cron = Constants.FIXED_DELAY)
	private void cleanup() {
		refreshTokenRepository.findAll().forEach(token -> {
			if (token.getExpirationDate().isAfter(Instant.now())) {
				refreshTokenRepository.delete(token);
			}
		});
	}

	@Override
	public void deleteToken(String input) {
		if(refreshTokenRepository.findByToken(input).isPresent()) {
		refreshTokenRepository.deleteById(input);
		}
	}
}
