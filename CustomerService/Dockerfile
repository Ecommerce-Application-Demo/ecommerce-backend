FROM gradle:8.5.0-jdk21-alpine

WORKDIR /app

COPY gradle/wrapper /gradle/wrapper
COPY build.gradle /app
COPY src /app/src
COPY settings.gradle /app
COPY gradlew /app

RUN gradle build -x test --no-daemon

CMD ["java", "-jar", "build/libs/CustomerService-0.0.1-SNAPSHOT.jar"]