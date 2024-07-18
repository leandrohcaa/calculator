FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/calculator-service-1.0.0.jar calculator-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "calculator-service.jar"]