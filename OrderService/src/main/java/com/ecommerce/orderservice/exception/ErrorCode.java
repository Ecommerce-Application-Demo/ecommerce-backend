package com.ecommerce.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    GENERAL_EXCEPTION(500,100,"Something went wrong"),
    PARAMETER_VALIDATION_FAILED(400,204,"Bad Request"),

    JWT_EXPIRED(401,125,"Provided JWT is Expired."),
    JWT_WRONG_SIGNATURE(401,126,"Provided JWT signature is not valid. Please provide valid JWT."),

    INVALID_ADDRESS_ID(400,132,"Given address is not valid."),

    INVALID_MOBILE(400,133,"Given mobile number is in invalid format."),
    INVALID_EMAIL(400,134,"Given email is in invalid format.");

    private final int httpStatusCode;
    private final int errorCode;
    private final String errorMessage;
}
