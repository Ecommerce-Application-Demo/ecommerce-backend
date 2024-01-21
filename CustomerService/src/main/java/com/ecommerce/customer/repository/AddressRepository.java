package com.ecommerce.customer.repository;

import org.springframework.data.repository.CrudRepository;

import com.ecommerce.customer.entity.Address;
import java.util.List;


public interface AddressRepository extends CrudRepository<Address, Integer> {
	
	List<Address> findAllByUserIdEmail(String userIdEmail);
	Address findByName(String name);
}
