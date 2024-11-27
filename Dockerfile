# Stage 1: Build the application
FROM maven:3.9.9-ibm-semeru-23-jammy AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=build /app/target/BlogApplication-0.0.1-SNAPSHOT.jar BlogApplication.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "BlogApplication.jar"]
