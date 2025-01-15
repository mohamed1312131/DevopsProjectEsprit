FROM openjdk:17-jdk-slim

WORKDIR /app

# Add a non-root user
RUN addgroup --system javauser && adduser --system --no-create-home --ingroup javauser javauser

COPY devops-0.0.1-20250105.142846-3.jar app.jar

# Change ownership of the app directory
RUN chown -R javauser:javauser /app

# Switch to non-root user
USER javauser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
