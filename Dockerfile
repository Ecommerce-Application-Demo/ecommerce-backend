FROM gradle:8.5-jdk21-alpine AS DEPS

WORKDIR /app
COPY . /app/

WORKDIR /app/CustomerService
RUN gradle build -x test

WORKDIR /app/Service-Registry
RUN gradle build -x test


FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=DEPS /app/CustomerService/build/libs /app/build
COPY --from=DEPS /app/Service-Registry/build/libs /app/build
COPY --from=DEPS /app/command.sh /app/

EXPOSE 8500 8761

CMD ["bash","/app/command.sh"]