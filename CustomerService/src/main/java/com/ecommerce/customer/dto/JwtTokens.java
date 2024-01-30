package com.ecommerce.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokens {
	private String accessToken;
	private String refreshToken;
	private String name;
	private String email;
}
