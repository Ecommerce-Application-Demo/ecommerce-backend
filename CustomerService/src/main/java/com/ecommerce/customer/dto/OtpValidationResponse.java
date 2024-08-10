package com.ecommerce.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpValidationResponse {
    private Boolean isValid;
    private Boolean isRegistered;
    private JwtTokens tokens;
}
