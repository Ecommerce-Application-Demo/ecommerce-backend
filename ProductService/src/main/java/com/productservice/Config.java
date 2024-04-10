package com.productservice;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Product Service")
                        .description("JAVA Backend API Documentation for Ecommerce Product Service")
                        .version("v0.1")
                        .license(new License().name("MIT").url("https://opensource.org/license/mit")))
                .externalDocs(new ExternalDocumentation()
                        .description("Github")
                        .url("https://github.com/Ecommerce-Application-Demo/ecommerce-backend"));
    }
}
