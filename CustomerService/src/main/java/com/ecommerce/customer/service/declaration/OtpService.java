package com.ecommerce.customer.service.declaration;

import com.ecommerce.customer.dto.OtpDetailsDto;
import com.ecommerce.customer.exception.CustomerException;
import jakarta.mail.MessagingException;

public interface OtpService {

	Integer generateOtp(String email);
	boolean validateOtp(OtpDetailsDto otpDetailsDto);
	void sendOtpByEmail(String email, String otp) throws MessagingException;

	
}
