# Build stage
FROM maven:3-eclipse-temurin-22-alpine AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:22-slim-bookworm
MAINTAINER xpakx.github.io
EXPOSE 8080
COPY --from=build app/target/images-integration-0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java","-jar","/application.jar"]
