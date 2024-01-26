package com.ecommerce.customer.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ecommerce.customer.dto.AddressDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.AddressRepository;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.customer.service.declaration.CustomerDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.customer.entity.Address;
import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.entity.CustomerAuth;

@Service
@Transactional
public class CustomerDetailsServiceImpl implements CustomerDetailsService {

	@Autowired
    CustomerRepository customerRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
    CustomerAuthRepository customerAuthRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
    AddressRepository addressRepository;
	
	@Override
	public String getUser() throws CustomerException {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (!(authentication instanceof AnonymousAuthenticationToken)) {
	    return authentication.getName();
	}else{
	    throw new CustomerException("CUSTOMER.NOT.FOUND",HttpStatus.NOT_FOUND);
	}
	}
	
	@Override
	public CustomerDto customerDetails(String email) {
		Customer customer = customerRepository.findByEmail(email).get();
		return modelMapper.map(customer,CustomerDto.class) ;
	}

	@Override
	public Boolean passwordVerify(String password) throws CustomerException {
		String dbPassword = customerAuthRepository.findById(getUser()).get().getPassword();
		return passwordEncoder.matches(password, dbPassword);
	}

	@Override
	public Boolean changePassword(String password) throws CustomerException {
		try {
		String newPassword = password;
		CustomerAuth customer= customerAuthRepository.findById(getUser()).get();
		customer.setPassword(passwordEncoder.encode(newPassword));
		customerAuthRepository.save(customer);
		return true;
		} catch (Exception e) {
			throw new CustomerException("PASSWORD.UPDATE.ERROR",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public AddressDto addAddress(AddressDto addressDto) throws CustomerException {
		Address address=modelMapper.map(addressDto,Address.class);
		address.setUserIdEmail(getUser());
		address.setAddCustomer(customerRepository.findByEmail(getUser()).get());
		address= addressRepository.save(address);
		return modelMapper.map(address, AddressDto.class);
	}

	@Override
	public List<AddressDto> getAddress() throws CustomerException {
		List<Address> address= addressRepository.findAllByUserIdEmail(getUser());
		List<AddressDto> addDto=new ArrayList<>();
		for (Address address2 : address) {
			addDto.add(modelMapper.map(address2,AddressDto.class));
		}
		return addDto;
	}

	@Override
	public Boolean deleteAcc() throws CustomerException {
		customerRepository.delete(customerRepository.findByEmail(getUser()).get());
		return true;
	}

	@Override
	public CustomerDto editDetails(CustomerDto customerDto) throws CustomerException {
		Customer customer =modelMapper.map(customerDto,Customer.class);
		if(customerRepository.existsById(customer.getUserId())) {
		customerRepository.save(customer);
		return customerDto;
		}
		else {
			throw new CustomerException("INVALID.USER.ID", HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public AddressDto editAddress(AddressDto addressDto) throws CustomerException {
		Address address=modelMapper.map(addressDto,Address.class);
		if(addressRepository.equals(address.getAddId())) {
			addressRepository.save(address);
		return addressDto;
		}
		else {
			throw new CustomerException("INVALID.ADDRESS.ID", HttpStatus.NOT_FOUND);
		}
	}

}
