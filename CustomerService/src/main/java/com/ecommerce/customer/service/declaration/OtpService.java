package com.ecommerce.customer.service.declaration;

import com.ecommerce.customer.dto.OtpDetailsDto;

public interface OtpService {

	Integer generateOtp(String email);
	boolean validateOtp(OtpDetailsDto otpDetailsDto);
	void sendOtpByEmail(String email, String otp);

	
}
