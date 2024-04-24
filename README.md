# E-Commerce Backend for Fashion Products (In Progress)

This project is a web-based e-commerce application backend designed to facilitate the purchase of fashion products online. It offers various features including user registration, login/logout, profile management, address management, email OTP verification, multi-device logout, and product management via JWT-secured REST APIs. Future plans include the implementation of cart, order, payment, and return functionalities.

- This is a Consolidated Repository of the E-commerce  Microservice. Find Individual Repo links below.

## Technologies Used

- Java SE 21
- Spring Boot 3.2
- PostgreSQL
- Gradle
- Spring REST
- Spring Data JPA
- JWT

## Features

- User Management:

  - User registration with email verification.
  - Login and logout functionalities.
  - Secure user profile and address management.
  - Email-based OTP verification for added security.
  - Multi-device logout for user account protection.
- Product Management:
  - Ability to add All Categories, Product & SKUs.
  - Get Products & SKUs based on custom filter.
- Planned Features:
  - Shopping cart functionality.
  - Order processing and management.
  - Payment integration.
  - Return and exchange functionalities.

## Project Structure

The project replicates microservice architecture . It is **hosted on Render.com using Docker containerization**.

Microservices can be accessed using [API Gateway](https://github.com/Ecommerce-Application-Demo/api-gateway) which implements **Circuit breaker, Rate Limiting & Load balancing**.

## Links of Each Repository

### &emsp; Github Repository &emsp; ------ ------ &emsp;Swagger Documentation
- [Customer Service Repo](https://github.com/Ecommerce-Application-Demo/ecommerce_backend-customer_service.git) &emsp;&emsp; ------ ------ &emsp;&emsp; [Customer Service Swagger](https://ecommerce-backend-dev.onrender.com/user/swagger-ui/index.html)
- [Product Service Repo](https://github.com/Ecommerce-Application-Demo/ecommerce_backend-product_service.git) &emsp;&emsp;&emsp; ------ ------ &emsp;&emsp; [Product Service Swagger](https://ecommerce-backend-dev.onrender.com/product/swagger-ui/index.html)

## Live Website

The backend powers the live e-commerce website [DesiCart](https://www.desicart.vercel.app) , which is built with React.js by [Kingshuk Roy](https://github.com/kingoroy)  
(Note: Limited functionality as backend features are being implemented)

## Getting Started

To run this project locally, follow these steps:

1. Clone this repository.
2. Set up a PostgreSQL database.
3. Configure the database connection in the application properties.
4. Setup application-local.properties file with required DB connection details & environment variables.
5. Build and run the application using Gradle.

## Contributions

Contributions to this project are welcome. Feel free to open issues or submit pull requests to help improve the functionality and maintainability of the codebase.  
You can mail me on: mazumdersourav2000@gmail.com

## License

This project is licensed under the [MIT License](LICENSE).
