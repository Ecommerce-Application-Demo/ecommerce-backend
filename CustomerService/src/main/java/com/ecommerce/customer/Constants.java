package com.ecommerce.customer;

public class Constants {
	
	public static final String JWT_HEADER_PREFIX = "Bearer ";
	
	/**
	 *  DB cleanup process in Cron.
	 *  Current Time : 6am everyday
	 */
	public static final String FIXED_DELAY= "0 0 6 * * *";
}
