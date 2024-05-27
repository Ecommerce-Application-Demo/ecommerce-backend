package com.ecommerce.customer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    GENERAL_EXCEPTION(500,100,"Something went wrong"),

    USER_NOT_FOUND(404,111,"User with this email id does not exist."),
    USER_DISABLED(403,114,"User has been disabled."),
    EMAIL_ALREADY_EXISTS(422,116,"Email Id already in use. Try with another email."),
    PASSWORD_UPDATE_ERROR(500,119,"Could not change the password. Try again."),

    REFRESH_TOKEN_NOT_FOUND(404,122, "Refresh token not found."),
    REFRESH_TOKEN_EXPIRED(403,123,"Refresh Token is Expired."),
    JWT_EXPIRED(401,125,"Provided JWT is Expired."),
    JWT_WRONG_SIGNATURE(401,126,"Provided JWT signature is not valid. Please provide valid JWT."),


    INVALID_CREDENTIAL(401,127,"User email or Password is not valid."),
    INVALID_OTP(400,128,"Invalid OTP"),
    INVALID_USER_ID(400,130,"Given user id is not valid."),
    INVALID_ADDRESS_ID(400,132,"Given address is not valid."),

    INVALID_MOBILE(400,133,"Given mobile number is in invalid format."),
    INVALID_EMAIL(400,134,"Given email is in invalid format."),
    INVALID_PASSWORD(400,135,"Password must contain 8 or more character with atleast one uppercase,lowercase,special and number character"),

    EMAIL_NOT_BLANK(400,140,"Email can not be blank"),
    PASSWORD_NOT_BLANK(400,141,"Password can not be blank");


    private final int httpStatusCode;
    private final int errorCode;
    private final String errorMessage;
}
