package com.ecommerce.orderservice.exception;

import java.io.Serial;

public class OrderException extends Exception {
	
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 7252602450875824238L;

	public OrderException(String message) {
		super(message);
	}
	

}
