package com.ecommerce.orderservice.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

	@Autowired
	Environment environment;

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> customerException(OrderException ex) {
		ErrorCode ec = ErrorCode.valueOf(ex.getMessage());
		ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),ex.getMessage(),ec.getErrorCode());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> allOtherException(Exception ex) {
		log.error(ex.getMessage(),ex);
		ErrorResponse response = new ErrorResponse(ErrorCode.GENERAL_EXCEPTION.getErrorMessage(),
				ex.getMessage(), ErrorCode.GENERAL_EXCEPTION.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
		List<String> exception=new ArrayList<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->exception.add(error.getDefaultMessage()));
		ErrorResponse response = new ErrorResponse(String.join(", ",exception),null,ErrorCode.PARAMETER_VALIDATION_FAILED.getErrorCode());
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST) ;
        }
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> expiredJwtException(ExpiredJwtException ex) {
		log.warn(ex.getMessage());
		ErrorCode ec = ErrorCode.JWT_EXPIRED;
		ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),null,ec.getErrorCode());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> jwtSignatureException(SignatureException ex) {
		log.warn(ex.getMessage());
		ErrorCode ec = ErrorCode.JWT_WRONG_SIGNATURE;
		ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),null,ec.getErrorCode());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));
	}
	
}
