package com.ecommerce.customer.repository;

import org.springframework.data.repository.CrudRepository;
import com.ecommerce.customer.entity.JwtRefreshToken;
import java.util.Optional;


public interface RefreshTokenRepository extends CrudRepository<JwtRefreshToken, String> {
	Optional<JwtRefreshToken> findByToken(String token);
	void deleteByEmail(String email);
}
