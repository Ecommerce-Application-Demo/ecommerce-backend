package com.ecommerce.customer.service.declaration;

import com.ecommerce.customer.entity.JwtRefreshToken;
import com.ecommerce.customer.exception.CustomerException;

import java.time.Instant;

public interface RefreshTokenService {

	String getRefreshToken(String emailDto);

	JwtRefreshToken extendTokenTime(String token) throws CustomerException;

	String tokenValidation(String refreshToken) throws CustomerException;

	void deleteToken(String input) throws CustomerException;

	Instant extractExpiration(String token) throws CustomerException;
}
