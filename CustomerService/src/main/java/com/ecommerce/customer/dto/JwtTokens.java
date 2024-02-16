package com.ecommerce.customer.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokens {
	private String accessToken;
	private String refreshToken;
	private Instant refreshTokenExpiration;
	private String name;
	private String email;
}
