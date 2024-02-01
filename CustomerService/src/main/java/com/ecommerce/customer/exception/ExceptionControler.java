package com.ecommerce.customer.exception;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
@Slf4j
public class ExceptionControler {

	@Autowired
	Environment environment;

	@ExceptionHandler
	public ResponseEntity<String> generalException(CustomerException ex) {
		log.info(ex.getMessage());
		return new ResponseEntity<>(environment.getProperty(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler
	public ResponseEntity<String> userNameException(UsernameNotFoundException ex) {
		log.info(ex.getMessage());
		return new ResponseEntity<>(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler
	public ResponseEntity<String> allOtherException(Exception ex) {
		log.info(ex.getMessage(),ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler
	public ResponseEntity<String> credentialException(BadCredentialsException ex) {
		log.info(ex.getMessage());
		return new ResponseEntity<>(environment.getProperty("INVALID.CREDENTIAL"), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> exception=new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{ 
        	log.info(error.getDefaultMessage());
        	exception.add(error.getDefaultMessage());
        		});
        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST) ;   
        }
	
	@ExceptionHandler
	public ResponseEntity<String> servletException(ServletException ex) {
		log.info(ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler
	public ResponseEntity<String> expiredJwtException(ExpiredJwtException ex) {
		log.info(ex.getMessage());
		return new ResponseEntity<>(environment.getProperty("JWT.EXPIRED"), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler
	public ResponseEntity<String> jwtSignatureException(SignatureException ex) {
		log.info(ex.getMessage());
		return new ResponseEntity<>(environment.getProperty("JWT.WRONG.SIGNATURE"), HttpStatus.BAD_REQUEST);
	}
	
}
