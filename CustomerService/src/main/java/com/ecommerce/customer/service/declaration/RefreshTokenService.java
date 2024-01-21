package com.ecommerce.customer.service.declaration;

import com.ecommerce.customer.dto.StringInputDto;
import com.ecommerce.customer.exception.CustomerException;

public interface RefreshTokenService {

	String getRefreshToken(String emailDto);

	String tokenValidation(StringInputDto refreshToken) throws CustomerException;

	void deleteToken(String input);
}
