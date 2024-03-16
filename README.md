# E-Commerce Backend for Fashion Products (In Progress)

This project is a web-based e-commerce application backend designed to facilitate the purchase of fashion products online. It offers various features including user registration, login/logout, profile management, address management, email OTP verification, multi-device logout, and product management via JWT-secured REST APIs. Future plans include the implementation of cart, order, payment, and return functionalities.

## Technologies Used

- Java SE 21
- Spring Boot 3.2
- PostgreSQL
- Gradle
- Spring REST
- Spring Data JPA
- JWT
- Elasticsearch

 ## Features
 
- User Management:

  - User registration with email verification.
  - Login and logout functionalities.
  - Secure user profile and address management.
  - Email-based OTP verification for added security.
  - Multi-device logout for user account protection.
- Product Management:
  - Ability to add and view product information.
- Planned Features:
  - Shopping cart functionality.
  - Order processing and management.
  - Payment integration.
  - Return and exchange functionalities.

## Project Structure

The project replicates microservice architecture within a single multi-module repository for optimized hosting resources. It is hosted on Render.com using Docker containerization.

## Swagger Documentation

The Swagger documentation for this project's APIs :  
[Customer Service](https://ecommerce-backend-dev.onrender.com/user/swagger-ui/index.html)

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
