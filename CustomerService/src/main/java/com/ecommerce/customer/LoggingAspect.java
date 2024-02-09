package com.ecommerce.customer;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
	
	@AfterThrowing(pointcut = "execution(* com.ecommerce.customer.controller.*.*(..))",throwing = "exception")
	public void controllerException(Exception exception) {
		log.error(exception.getMessage(),exception);
	}
	
	@AfterThrowing(pointcut = "execution(* com.ecommerce.customer.service.*.*(..))",throwing = "exception")
	public void serviceException(Exception exception) {
		log.error(exception.getMessage(),exception);
	}
	
}
