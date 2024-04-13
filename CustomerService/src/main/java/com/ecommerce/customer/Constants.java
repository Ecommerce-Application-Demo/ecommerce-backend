package com.ecommerce.customer;

public class Constants {
	
	public static final String JWT_HEADER_PREFIX = "Bearer ";
	
	/**
	 *  DB cleanup process in Cron.
	 *  Current Time : 6am everyday
	 */
	public static final String FIXED_DELAY= "0 0 6 * * *";

	public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~])[A-Za-z\\d!\"#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~]{8,}$";

	public static final String EMAIL_REGEX = "[A-Za-z0-9-\\.]+@[A-Za-z0-9]+\\.[A-Za-z]+";

	public static final String PHONE_NUMBER_REGEX = "^(?=[6-9])\\d{10}$";
}
