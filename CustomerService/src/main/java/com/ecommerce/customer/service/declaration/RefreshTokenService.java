package com.ecommerce.customer.service.declaration;

import com.ecommerce.customer.exception.CustomerException;

public interface RefreshTokenService {

	String getRefreshToken(String emailDto);

	String tokenValidation(String refreshToken) throws CustomerException;

	void deleteToken(String input);
}
