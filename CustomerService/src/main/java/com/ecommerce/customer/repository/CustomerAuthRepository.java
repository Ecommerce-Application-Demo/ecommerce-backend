package com.ecommerce.customer.repository;

import com.ecommerce.customer.entity.CustomerAuth;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerAuthRepository extends CrudRepository<CustomerAuth, String> {
	Optional<CustomerAuth> findByEmail(String email);

	@Modifying
	@Query(value = "CALL customer.invalidate_tokens(?1,?2)",nativeQuery = true)
	void invalidateTokens(String email,String salt);
}
