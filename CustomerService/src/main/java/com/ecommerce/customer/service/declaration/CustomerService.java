package com.ecommerce.customer.service.declaration;

import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.exception.CustomerException;

import jakarta.validation.constraints.NotBlank;

public interface CustomerService {

	void registerNewCustomer(CustomerDto customerDto) throws CustomerException;

	String welcomeService(String email);

	Boolean isPresent(@NotBlank String input);

}
