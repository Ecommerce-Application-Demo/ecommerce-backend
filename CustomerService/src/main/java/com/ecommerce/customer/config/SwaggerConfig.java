package com.ecommerce.customer.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "Bearer Authentication",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                scheme = "bearer"
                )
public class SwaggerConfig {

    @Bean
    OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Customer Service")
                        .description("JAVA Backend API Documentation for Ecommerce Customer Service")
                        .version("v0.1")
                        .license(new License().name("MIT License").url("https://opensource.org/license/mit")))
                .externalDocs(new ExternalDocumentation()
                        .description("Github")
                        .url("https://github.com/Ecommerce-Application-Demo/ecommerce-backend"));
    }
}
