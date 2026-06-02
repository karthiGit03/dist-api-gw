# Use Java 25
FROM eclipse-temurin:25

WORKDIR /app

# Copy Spring Boot JAR
COPY target/*.jar app.jar

# Gateway exposes 6060
EXPOSE 5050

ENTRYPOINT ["java","-jar","app.jar"]