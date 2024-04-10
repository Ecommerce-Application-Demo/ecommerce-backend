package com.productservice.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleUniqueConstraintViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Unique constraint violation";
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            String constraintName = ((ConstraintViolationException) ex.getCause()).getConstraintName();
            errorMessage = "Violation of unique constraint '" + constraintName + "'";
            log.error(errorMessage, ex);
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> allOtherException(Exception ex) {
        log.warn(ex.getMessage(),ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
