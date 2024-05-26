package com.ecommerce.customer.service.declaration;

import com.ecommerce.customer.dto.AddressDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.exception.CustomerException;

import java.util.List;

public interface CustomerDetailsService {

	CustomerDto customerDetails(String string);
	Boolean passwordVerify(String password) throws CustomerException;
	String changePassword(String password) throws CustomerException;
	String getUser() throws CustomerException;
	List<AddressDto> getAddress() throws CustomerException;
	AddressDto addAddress(AddressDto addressDto) throws CustomerException;
	Boolean deleteAcc() throws CustomerException;
	CustomerDto editDetails(CustomerDto customerDto)throws CustomerException;
	AddressDto editAddress(AddressDto addressDto) throws CustomerException;
	void deleteAddress(int addId) throws CustomerException;
	void changeEmail (String email) throws CustomerException;
    Boolean invalidateAllToken() throws CustomerException;
}
