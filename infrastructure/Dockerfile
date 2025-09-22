# Start from a lightweight Alpine-based OpenJDK 21 image
FROM eclipse-temurin:21-jdk-alpine

# Set a working directory inside the container
WORKDIR /app

# Copy the Spring Boot fat JAR into the container
COPY document-storage*.jar document-storage.jar

# Expose the application port (adjust if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "document-storage.jar"]
