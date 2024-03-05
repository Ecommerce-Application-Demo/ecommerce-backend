package com.ecommerce.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import com.ecommerce.customer.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	Optional<Customer> findByEmail(String email);
	
	@Procedure(procedureName = "update_email")
	void updateEmail(String oldemail,String newemail,int userid);
}
