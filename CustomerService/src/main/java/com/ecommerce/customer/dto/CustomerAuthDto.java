package com.ecommerce.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerAuthDto {

	private String email;
	@NotNull
	private String password;

}
