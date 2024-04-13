package com.ecommerce.customer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ecommerce.customer.dto.AddressDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.AddressRepository;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.customer.repository.DefaultAddressRepository;
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
import com.ecommerce.customer.entity.DefaultAddress;

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
	@Autowired
	DefaultAddressRepository defaultAddressRepository;
	
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
		Customer customer = customerRepository.findByEmail(email.toLowerCase()).get();
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
		invalidateAllToken();
		return true;
		} catch (Exception e) {
			throw new CustomerException("PASSWORD.UPDATE.ERROR",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean invalidateAllToken() throws CustomerException {
		customerAuthRepository.invalidateTokens(getUser(),UUID.randomUUID().toString().replace("-",""));
		return true;
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
			customer.setEmail(customer.getEmail().toLowerCase());
			customerRepository.save(customer);
			return customerDto;
		}
		else {
			throw new CustomerException("INVALID.USER.ID", HttpStatus.NOT_FOUND);
		}
	}
	
	@Override
	public void changeEmail(String email,Integer userId) throws CustomerException {
			customerRepository.updateEmail(getUser(), email.toLowerCase(), userId);
	}


	@Override
	public AddressDto addAddress(AddressDto addressDto) throws CustomerException {
		Address address=modelMapper.map(addressDto,Address.class);
		String user=getUser();
		address.setUserIdEmail(user);
		address.setAddCustomer(customerRepository.findByEmail(user).get());
		address= addressRepository.save(address);
		if(addressDto.isDefault() || defaultAddressRepository.findById(user).isEmpty()) {
			DefaultAddress add= new DefaultAddress(user,address.getAddId());
			defaultAddressRepository.save(add);
		}
		
		return modelMapper.map(address, AddressDto.class);
	}

	@Override
	public List<AddressDto> getAddress() throws CustomerException {
		String user=getUser();
		List<Address> address= addressRepository.findAllByUserIdEmail(user);
		List<AddressDto> addDto=new ArrayList<>();
		if(address.isEmpty())
			return addDto;
		int id=defaultAddressRepository.findById(user).get().getAddId();
		addDto= address.stream()
			   .map(add-> modelMapper.map(add,AddressDto.class)) 
			   .map(add2-> { if(add2.getAddId()==id){ 
				   				add2.setDefault(true);
				   				}
			   					return add2;
			   			   }).toList();
		return addDto;
	}

	@Override
	public AddressDto editAddress(AddressDto addressDto) throws CustomerException {
		Address address=modelMapper.map(addressDto,Address.class);
		if(addressRepository.existsById((address.getAddId()))) {
			address.setUserIdEmail(getUser());
			addressRepository.save(address);
			if(addressDto.isDefault()) {
				DefaultAddress add= new DefaultAddress(getUser(),address.getAddId());
				defaultAddressRepository.save(add);
			}
		return addressDto;
		}
		else {
			throw new CustomerException("INVALID.ADDRESS.ID", HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void deleteAddress(int addId) throws CustomerException {
		if(addressRepository.existsById(addId)) {
			addressRepository.deleteById(addId);
		}
		else {
			throw new CustomerException("INVALID.ADDRESS.ID", HttpStatus.NOT_FOUND);
		}
	}

}
