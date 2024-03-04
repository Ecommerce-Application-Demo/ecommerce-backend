package com.ecommerce.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import com.ecommerce.customer.entity.CustomerAuth;

public interface CustomerAuthRepository extends CrudRepository<CustomerAuth, String> {
	Optional<CustomerAuth> findByEmail(String email);

	@Procedure(procedureName = "invalidate_tokens")
	void invalidateTokens(String email,String salt);
}
