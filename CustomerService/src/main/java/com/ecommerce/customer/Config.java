package com.ecommerce.customer;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@EnableScheduling
public class Config {

    @Bean
    ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	 @Bean
	  public OpenAPI springShopOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("Ecommerce Backend")
	              .description("JAVA Backend API Documentation")
	              .version("v0.0.1")
	              .license(new License().name("Apache 2.0").url("http://springdoc.org")))
	              .externalDocs(new ExternalDocumentation()
	              .description("Github")
	              .url("https://github.com/Ecommerce-Application-Demo/ecommerce-backend"));
	  }

}
