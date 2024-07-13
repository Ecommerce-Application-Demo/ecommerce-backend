package com.ecommerce.customer.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@EnableScheduling
public class Config {

	@Autowired
	Environment env;

    @Bean
    ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Scheduled(fixedDelay = 1000*60*5)
	void renderRunner() {
		if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getForEntity("https://ecommerce-backend-customer-service.onrender.com/user/api/auth/index", String.class);
		}
	}
}
