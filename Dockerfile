# Use official OpenJDK base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy JAR file built by Maven
COPY target/document-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8082

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]