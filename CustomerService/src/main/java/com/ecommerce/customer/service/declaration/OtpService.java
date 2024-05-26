package com.ecommerce.customer.service.declaration;

import jakarta.mail.MessagingException;

public interface OtpService {

	Integer generateOtp(String email);
	boolean validateOtp(String email, int otp);
	void sendOtpByEmail(String email, String otp) throws MessagingException;

	
}
