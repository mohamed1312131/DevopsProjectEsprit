# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the downloaded JAR file into the container
COPY devops-0.0.1-20250105.142846-3.jar app.jar

# Expose the port your app uses
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
