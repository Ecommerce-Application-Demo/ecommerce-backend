package com.ecommerce.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
	private int addId;
	@NotNull
	private String name;
	private Integer phoneNumber;
	private String addressLine1;
	@NotNull
	private Long pincode;
	private String state;
	private String locality;
	private String city;
	private String addressType;
	private boolean isDefault = false;

}
