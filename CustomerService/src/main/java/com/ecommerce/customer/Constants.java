package com.ecommerce.customer;

public class Constants {

	/**
	 * JWT Token Validity in Millisecond.
	 * Current Time : 30Min
	 * 
	 */
	public static final Long JWT_VALIDITY = 1000 * 60*30L;
	public static final String JWT_SECRET = "YhiD5iVhUuArth8thDavM/H7LH6oGAck6QTbkIfTXcLEG+hRWsVmUou+XoyfiIND";
	public static final String JWT_HEADER_PREFIX = "Bearer ";
	/**
	 * Refresh Token Validity in Millisecond.
	 * Current Time : 1 day
	 */
	public static final int REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24;
	/**
	 *  Email OTP Validity in Minute.
	 *  Current Time : 60Min
	 */
	public static final Long OTP_VALIDITY = 60L;
	/**
	 *  DB cleanup process in Milliseconds.
	 *  Current Time : 1hour
	 */
	public static final long FIXED_DELAY = 1000*60*60*1;
}
