FROM gradle:8.5-jdk21-alpine AS DEPS

WORKDIR /app

COPY /ProductService/ /app/ProductService

WORKDIR /app/ProductService

RUN gradle build -x test --no-daemon

# Use the official OpenJDK base image for Java 21
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar to the container
COPY --from=DEPS /app/ProductService/build/libs /app/build

# Expose the port that your Spring Boot application will run on
EXPOSE 8520

# Specify the command to run on container startup
CMD ["java", "-jar", "/app/build/ProductService.jar"]