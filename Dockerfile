FROM openjdk:17-jdk-slim

WORKDIR /app

# Add a non-root user
RUN addgroup --system javauser && adduser --system --no-create-home --ingroup javauser javauser

# Copy the application JAR into the container
COPY devops-0.0.1-20250105.142846-3.jar app.jar

# Change ownership of the app directory
RUN chown -R javauser:javauser /app

# Switch to non-root user
USER javauser

# Expose the application port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
