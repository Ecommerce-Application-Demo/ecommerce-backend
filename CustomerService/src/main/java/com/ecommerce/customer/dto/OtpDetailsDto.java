package com.ecommerce.customer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpDetailsDto {

	private String email;
	private int otp;
}
