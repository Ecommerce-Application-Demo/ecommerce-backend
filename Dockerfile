FROM gradle:8.7-jdk21-alpine AS DEPS

WORKDIR /app

COPY . /app/

WORKDIR /app/CustomerService
RUN gradle build -x test --no-daemon

WORKDIR /app/ProductService
RUN gradle build -x test --no-daemon

# Use the official OpenJDK base image for Java 21
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar to the container

COPY --from=DEPS /app/CustomerService/build/libs /app/build
COPY --from=DEPS /app/ProductService/build/libs /app/build
COPY --from=DEPS /app/command.sh /app/

# Expose the port that your Spring Boot application will run on
EXPOSE 8500 8520

# Specify the command to run on container startup
CMD ["bash","/app/command.sh"]