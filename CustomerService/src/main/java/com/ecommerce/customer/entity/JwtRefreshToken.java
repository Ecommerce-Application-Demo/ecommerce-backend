package com.ecommerce.customer.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JwtRefreshToken {

	
	private String email;
	@Id
	private String token;
	private Instant expirationDate;
	
}
