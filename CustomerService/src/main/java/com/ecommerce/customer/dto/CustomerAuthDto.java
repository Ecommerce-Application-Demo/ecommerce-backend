package com.ecommerce.customer.dto;

import com.ecommerce.customer.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerAuthDto {

	@NotNull(message = "EMAIL_NOT_BLANK")
	@Pattern(regexp = Constants.EMAIL_REGEX, message = "INVALID_EMAIL")
	private String email;
	@NotNull
	@Pattern(regexp = Constants.PASSWORD_REGEX, message = "INVALID_PASSWORD")
	private String password;

}
