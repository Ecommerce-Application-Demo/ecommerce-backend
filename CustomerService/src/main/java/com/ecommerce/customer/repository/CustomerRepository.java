package com.ecommerce.customer.repository;

import com.ecommerce.customer.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	Optional<Customer> findByEmail(String email);

	@Transactional
	@Modifying
	@Query(value = "CALL customer.update_email(?1,?2)",nativeQuery = true)
	void updateEmail(String oldEmail,String newEmail);

	@Transactional
	@Modifying
	@Query(value = "CALL customer.delete_account(?1)",nativeQuery = true)
	void deleteAccount(String userEmail);
}
