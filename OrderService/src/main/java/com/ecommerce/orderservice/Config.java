package com.ecommerce.orderservice;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@SecurityScheme(name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class Config {

    @Value("${product.base.url}")
    private String productBaseUrl;

    @Autowired
    Environment env;


    @Bean
    RestClient productRestClient() {
        return RestClient.builder()
                .baseUrl(productBaseUrl)
                .defaultHeader(Constants.PRODUCT_API_KEY, Constants.PRODUCT_API_SECRET)
                .build();
    }

    @Bean
    OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Order Service")
                        .description("JAVA Backend API Documentation for Ecommerce Order Service")
                        .version("v0.1")
                        .license(new License().name("MIT License").url("https://opensource.org/license/mit")))
                .externalDocs(new ExternalDocumentation()
                        .description("Github")
                        .url("https://github.com/Ecommerce-Application-Demo/ecommerce-backend"));
    }

    @Scheduled(fixedDelay = 1000*60*5)
    void keepAlive() {
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForEntity("https://ecommerce-backend-order-service.onrender.com/order/actuator/info", String.class);
        }
    }
}
