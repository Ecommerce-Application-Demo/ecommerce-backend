package com.ecommerce.customer.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpDetailsDto {

	private String email;
	private int otp;
	private LocalDateTime otpTime;
}
