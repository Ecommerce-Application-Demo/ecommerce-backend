# API Gateway

The API Gateway serves as the central entry point for client requests to access [**E-commerce Backend**](https://github.com/Ecommerce-Application-Demo/ecommerce-backend).

## Key Features

- ### Routing:
    Directs incoming requests to the appropriate microservice with **load balancing** based on predefined routes and predicates with the help of [Eureka Service Registry](https://github.com/Ecommerce-Application-Demo/service-registry)
- ### Resilience:
    Implements resilience patterns with **Resilience4J** such as **circuit breaker** and **rate limiting** to enhance system robustness and reliability.

## Configuration

Configuration settings for the API Gateway are managed via the `application.properties` file. It includes settings for Actuator endpoints, API routes,Eureka Server DefaultZone URL, Circuit Breaker & Rate limiter configurations etc.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
