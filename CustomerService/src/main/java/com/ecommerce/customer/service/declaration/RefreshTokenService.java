package com.ecommerce.customer.service.declaration;

import java.time.Instant;
import com.ecommerce.customer.exception.CustomerException;

public interface RefreshTokenService {

	String getRefreshToken(String emailDto);

	String tokenValidation(String refreshToken) throws CustomerException;

	void deleteToken(String input) throws CustomerException;

	Instant extractExpiration(String token) throws CustomerException;
}
