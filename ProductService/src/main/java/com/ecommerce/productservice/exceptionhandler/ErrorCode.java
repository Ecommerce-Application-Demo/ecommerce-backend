package com.ecommerce.productservice.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    GENERAL_EXCEPTION(500,100,"Something went wrong"),
    PARAMETER_VALIDATION_FAILED(400,204,"Bad Request"),

    API_KEY_NOT_PASSED(401,211,"API Key is not passed"),
    INVALID_API_SECRET(401,214,"API Secret is Invalid"),
    INVALID_PAGINATION(400,217,"Page number & Products per page fields must be greater than 0"),

    INVALID_EMAIL(400,134,"Given email is in invalid format."),
    EMAIL_NOT_BLANK(400,140,"Email can not be blank"),
    INPUT_VALIDATION_FAILED(400,150,"Invalid input format");

    private final int httpStatusCode;
    private final int errorCode;
    private final String errorMessage;
}
