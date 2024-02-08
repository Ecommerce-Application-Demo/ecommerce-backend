package com.ecommerce.customer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDto {

	private int userId;
	@NotNull(message = "Email can not be blank")
	@Pattern(regexp = "[A-Za-z0-9-\\.]+@[A-Za-z0-9]+\\.[A-Za-z]+",message = "Email pattern does not match!")
	private String email;
	@NotNull
	private String name;
	@NotNull(message = "Password can not be null")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "Password must contain 8 or more character with atleast one uppercase,lowercase,special and number character")
	private String password;
	@Pattern(regexp="^(?=[6-9])\\d{10}$",message = "Mobile number invalid format")
	private String phoneNumber;
	private String gender;

}