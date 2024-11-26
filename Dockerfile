FROM maven:3.9.9-ibm-semeru-23-jammy AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:23-jdk-slim
COPY --from=build target/BlogApplication.jar BlogApplication.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "BlogApplication.jar"]
