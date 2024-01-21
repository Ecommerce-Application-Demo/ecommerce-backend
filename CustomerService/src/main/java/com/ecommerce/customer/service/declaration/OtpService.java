package com.ecommerce.customer.service.declaration;

import com.ecommerce.customer.dto.OtpDetailsDto;
import com.ecommerce.customer.dto.StringInputDto;

public interface OtpService {

	Integer generateOtp(StringInputDto email);
	boolean validateOtp(OtpDetailsDto otpDetailsDto);
	void sendOtpByEmail(String email, String otp);

	
}
