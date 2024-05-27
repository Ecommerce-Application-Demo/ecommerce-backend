package com.ecommerce.productservice.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> productException(ProductException ex) {
        ErrorCode ec = ErrorCode.valueOf(ex.getMessage());
        ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),ec.getErrorCode());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> allOtherException(Exception ex) {
        log.error(ex.getMessage(),ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.GENERAL_EXCEPTION.getErrorMessage()
                +". [ "+ex.getMessage()+" ]", ErrorCode.GENERAL_EXCEPTION.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> exception=new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            exception.add(error.getDefaultMessage());
        });
        ErrorResponse response = new ErrorResponse(String.join(", ",exception),ErrorCode.PARAMETER_VALIDATION_FAILED.getErrorCode());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleFormatException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(),ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INPUT_VALIDATION_FAILED.getErrorMessage()+". ["+
                                                    ex.getMessage()+" ]",ErrorCode.INPUT_VALIDATION_FAILED.getErrorCode() );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST) ;
    }
}
