package com.ecommerce.customer.repository;

import com.ecommerce.customer.entity.CustomerAuth;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerAuthRepository extends CrudRepository<CustomerAuth, String> {
	Optional<CustomerAuth> findByEmail(String email);
}
