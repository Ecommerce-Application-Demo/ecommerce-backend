package com.ecommerce.customer.dto;

import com.ecommerce.customer.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
	private int addId;
	@NotNull
	private String name;
	@Pattern(regexp= Constants.PHONE_NUMBER_REGEX, message = "INVALID_MOBILE")
	private String phoneNumber;
	private String addressLine1;
	@NotNull
	private String pincode;
	private String state;
	private String locality;
	private String city;
	private String addressType;
	private boolean isDefault = false;

}
