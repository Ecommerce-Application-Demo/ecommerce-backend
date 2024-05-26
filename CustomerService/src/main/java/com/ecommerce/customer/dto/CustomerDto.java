package com.ecommerce.customer.dto;

import com.ecommerce.customer.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDto {

	private int userId;
	@NotNull(message = "EMAIL_NOT_BLANK")
	@Pattern(regexp = Constants.EMAIL_REGEX, message = "INVALID_EMAIL")
	private String email;
	@NotNull
	private String name;
	@NotNull(message = "PASSWORD_NOT_BLANK")
	@Pattern(regexp = Constants.PASSWORD_REGEX, message = "INVALID_PASSWORD")
	private String password;
	@Pattern(regexp=Constants.PHONE_NUMBER_REGEX, message = "INVALID_MOBILE")
	private String phoneNumber;
	private String gender;

}