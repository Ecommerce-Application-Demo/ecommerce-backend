package com.ecommerce.customer.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	public ResponseEntity<ErrorResponse> customerException(CustomerException ex) {
		ErrorCode ec = ErrorCode.valueOf(ex.getMessage());
		ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),ec.getErrorCode());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> userNameException(UsernameNotFoundException ex) {
		ErrorCode ec = ErrorCode.valueOf(ex.getMessage());
		ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),ec.getErrorCode());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));

	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> allOtherException(Exception ex) {
		log.error(ex.getMessage());
		ErrorResponse response = new ErrorResponse(ErrorCode.GENERAL_EXCEPTION.getErrorMessage(), ErrorCode.GENERAL_EXCEPTION.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> credentialException(BadCredentialsException ex) {
		log.error(ex.getMessage());
		ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_CREDENTIAL.getErrorMessage(),ErrorCode.INVALID_CREDENTIAL.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse> exception=new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
			ErrorCode ec = ErrorCode.valueOf(error.getDefaultMessage());
			ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),ec.getErrorCode());
        	exception.add(response);
        		});
        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST) ;   
        }
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> expiredJwtException(ExpiredJwtException ex) {
		ErrorCode ec = ErrorCode.valueOf(ex.getMessage());
		ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),ec.getErrorCode());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> jwtSignatureException(SignatureException ex) {
		log.warn(ex.getMessage());
		ErrorCode ec = ErrorCode.JWT_WRONG_SIGNATURE;
		ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),ec.getErrorCode());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> messagingException(MessagingException ex) {
		ErrorResponse response = new ErrorResponse(ex.getMessage(),100);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> disabledException(DisabledException ex) {
		ErrorCode ec = ErrorCode.valueOf(ex.getMessage());
		ErrorResponse response = new ErrorResponse(ec.getErrorMessage(),ec.getErrorCode());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(ec.getHttpStatusCode()));
	}
	
}
