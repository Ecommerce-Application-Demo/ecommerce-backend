# E-Commerce Backend -- Customer Service

 Customer Service of root microservices project [E-commerce Backend](https://github.com/Ecommerce-Application-Demo/ecommerce-backend.git)

## Technologies Used

- Java SE 21
- Spring Boot 3.2
- PostgreSQL
- Gradle
- Spring REST
- Spring Data JPA
- JWT
- Docker

 ## Features
 
- User Management:

  - User registration with email verification.
  - Login and logout functionalities.
  - Secure user profile and address management.
  - Email-based OTP verification for added security.
  - Multi-device logout for user account protection.


## Project Structure

The project replicates microservice architecture . It is **hosted on Render.com using Docker containerization**.

Microservices can be accessed using [API Gateway](https://github.com/Ecommerce-Application-Demo/api-gateway) which implements **Circuit breaker, Rate Limiting & Load balancing**.

## Swagger Documentation

The Swagger documentation for this project's APIs : 

- [Customer Service](https://ecommerce-backend-dev.onrender.com/user/swagger-ui/index.html)

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
