# Use the official OpenJDK base image for Java 21
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar to the container
COPY ./build/libs/CustomerService-0.0.1-SNAPSHOT.jar /app

# Expose the port that your Spring Boot application will run on
EXPOSE 8500

# Specify the command to run on container startup
CMD ["java", "-jar", "CustomerService-0.0.1-SNAPSHOT.jar"]
