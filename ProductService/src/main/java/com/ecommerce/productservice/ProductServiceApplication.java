package com.ecommerce.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@PropertySource(value = { "classpath:messages.properties" })
@EnableScheduling
public class ProductServiceApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("GMT+05:30"));

		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
