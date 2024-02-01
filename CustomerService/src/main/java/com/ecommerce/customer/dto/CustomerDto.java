package com.ecommerce.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDto {

	private int userId;
	@NotNull(message = "Email can not be blank")
	private String email;
	@NotNull
	private String name;
	@NotNull(message = "Password can not be null")
	private String password;
	private Long phoneNumber;
	private String gender;

}