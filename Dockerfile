FROM openjdk:8-jdk-alpine
EXPOSE 8082
ADD target/*.jar devops-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/devops-0.0.1-SNAPSHOT.jar"]