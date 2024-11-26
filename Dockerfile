FROM maven:3.9.9-ibm-semeru-23-jammy AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:23-jdk-slim
COPY --from=build target/BlogAppication-0.0.1-SNAPSHOT.jar BlogApllication.jar
EXPOSE 8080
ENTRYPOINT["java","-jar","BlogApllication.jar"]