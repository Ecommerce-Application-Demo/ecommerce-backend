package com.ecommerce.customer.dto;

import com.ecommerce.customer.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ForgetPasswordDto(
        @NotNull
        int OTP,
        @NotNull(message = "EMAIL_NOT_BLANK")
        @Pattern(regexp = Constants.EMAIL_REGEX, message = "INVALID_EMAIL")
        String userEmail,
        @NotNull(message = "PASSWORD_NOT_BLANK")
        @Pattern(regexp = Constants.PASSWORD_REGEX, message = "INVALID_PASSWORD")
        String newPassword) {
}
