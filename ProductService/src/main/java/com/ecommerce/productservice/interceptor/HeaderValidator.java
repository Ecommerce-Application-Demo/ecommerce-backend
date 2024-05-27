package com.ecommerce.productservice.interceptor;

import com.ecommerce.productservice.exceptionhandler.ErrorCode;
import com.ecommerce.productservice.exceptionhandler.ProductException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;

public class HeaderValidator {

    public static void requestValidator(HttpServletRequest request,String apiKey, String apiSecret) throws ProductException {
        List<String> requestHeaders = Collections.list(request.getHeaderNames());
        if(!request.getMethod().equalsIgnoreCase("OPTIONS")) {
            if (!requestHeaders.contains(apiKey)) {
                throw new ProductException(ErrorCode.API_KEY_NOT_PASSED.name());
            } else {
                if (!request.getHeader(apiKey).equals(apiSecret)) {
                    throw new ProductException(ErrorCode.INVALID_API_SECRET.name());
                }
            }
        }
    }
}
