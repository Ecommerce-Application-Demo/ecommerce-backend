package com.ecommerce.customer.exception;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControler {

	@Autowired
	Environment environment;

	@ExceptionHandler
	public ResponseEntity<String> generalException(CustomerException ex) {
		return new ResponseEntity<>(environment.getProperty(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler
	public ResponseEntity<String> userNameException(UsernameNotFoundException ex) {
		return new ResponseEntity<>(environment.getProperty(ex.getMessage()), HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler
	public ResponseEntity<String> allOtherException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler
	public ResponseEntity<String> credentialException(BadCredentialsException ex) {
		return new ResponseEntity<>(environment.getProperty("INVALID.CREDENTIAL"), HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> exception=new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->exception.add(error.getDefaultMessage()));

        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST) ;   
        }
	
	@ExceptionHandler
	public ResponseEntity<String> servletException(ServletException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
