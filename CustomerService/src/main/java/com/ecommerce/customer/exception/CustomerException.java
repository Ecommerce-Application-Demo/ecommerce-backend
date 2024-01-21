package com.ecommerce.customer.exception;

import org.springframework.http.HttpStatus;

public class CustomerException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7252602450875824238L;

	public CustomerException(String message, HttpStatus status) {
		super(message);
	}
	

}
