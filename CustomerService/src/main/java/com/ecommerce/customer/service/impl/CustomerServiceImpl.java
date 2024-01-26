package com.ecommerce.customer.service.impl;

import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.customer.service.declaration.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.customer.entity.CustomerAuth;
import com.ecommerce.customer.entity.Customer;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
    CustomerRepository customerRepository;
	@Autowired
    CustomerAuthRepository customerAuthRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void registerNewCustomer(CustomerDto customerDto) throws CustomerException {

		boolean registeredEmail =customerRepository.findByEmail(customerDto.getEmail()).isEmpty();

		if (registeredEmail) {
			Customer customer = modelMapper.map(customerDto, Customer.class);
			customerRepository.save(customer);
			CustomerAuth customerAuth = new CustomerAuth();
			customerAuth.setEmail(customer.getEmail());
			customerAuth.setPassword(passwordEncoder.encode(customer.getPassword()));
			customerAuth.setAuthCustomer(customer);
			customerAuthRepository.save(customerAuth);
		} else {
			throw new CustomerException("EMAIL.ALREADY.EXISTS", HttpStatus.BAD_GATEWAY);
		}

	}

	@Override
	public String welcomeService(String email) {
		Customer customer = customerRepository.findByEmail(email).orElseThrow();
		return customer.getName();
	}

	@Override
	public Boolean isPresent(String email) {
		return customerRepository.findByEmail(email).isPresent();
	}

}
