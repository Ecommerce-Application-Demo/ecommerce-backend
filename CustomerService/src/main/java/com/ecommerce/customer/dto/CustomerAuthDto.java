package com.ecommerce.customer.dto;

import com.ecommerce.customer.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerAuthDto {

	@NotNull(message = "Email can not be blank")
	@Pattern(regexp = Constants.EMAIL_REGEX, message = "Email pattern does not match!")
	private String email;
	@NotNull
	@Pattern(regexp = Constants.PASSWORD_REGEX, message = "Password must contain 8 or more character with atleast one uppercase,lowercase,special and number character")
	private String password;

}
