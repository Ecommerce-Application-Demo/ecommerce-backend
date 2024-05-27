# E-Commerce Backend -- Product Service

Product Service of root microservices project [E-commerce Backend](https://github.com/Ecommerce-Application-Demo/ecommerce-backend.git)


## Technologies Used

- Java SE 21
- Spring Boot 3.2
- PostgreSQL
- Gradle
- Spring REST
- Spring Data JPA

 ## Features

- ### Product Management:
  - Full Text Search implementation using PostgreSQL query.
  - Get Products as per will from text search or filters.
  - Get product delivery time from nearest Warehouse, if deliverable at your pincode.
  - Ability to add All Categories,Product & it's Style variants.
  - Warehouse based inventory management.

## Project Structure

The project replicates microservice architecture . It is **hosted on Render.com using Docker containerization**.

Microservices can be accessed using [API Gateway](https://github.com/Ecommerce-Application-Demo/api-gateway) which implements **Circuit breaker, Rate Limiting & Load balancing**.

## Swagger Documentation

The Swagger documentation for this project's APIs : 

- [Product Service](https://ecommerce-backend-product-service.onrender.com/product/swagger-ui/index.html)

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
