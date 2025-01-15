pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'mohamedsalahmechergui'
        IMAGE_NAME = 'devops-app'
        IMAGE_VERSION = '0.0.1-20250105.142846-3'
        APP_PORT = '8089'
        COMPOSE_FILE = 'docker-compose.yml'
        PROJECT_NAME = 'devops'
    }

    stages {
        stage('Create Docker Compose File') {
            steps {
                script {
                    // Create docker-compose.yml
                    writeFile file: 'docker-compose.yml', text: '''version: '3.8'

services:
  app:
    image: devops-app:latest
    container_name: devops-app
    ports:
      - "${APP_PORT}:8089"
    environment:
      - SPRING_APPLICATION_NAME=devops
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/devops?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=
      - SPRING_JPA_SHOW_SQL=true
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SERVER_ADDRESS=0.0.0.0
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - devops-network

  mysql:
    image: mysql:8.0
    container_name: mysql-db
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ALLOW_EMPTY_PASSWORD=yes
      - MYSQL_DATABASE=devops
    volumes:
      - mysql-data:/var/lib/mysql
    command: --default-authentication-plugin=mysql_native_password
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 5s
      retries: 10
    networks:
      - devops-network

networks:
  devops-network:
    driver: bridge

volumes:
  mysql-data:'''
                }
            }
        }

        stage('Pull Images') {
            steps {
                script {
                    sh """
                        docker pull ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_VERSION}
                        docker pull mysql:8.0
                    """
                }
            }
        }

        stage('Tag Image') {
            steps {
                script {
                    sh """
                        docker tag ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_VERSION} ${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    // Create .env file for docker compose
                    sh """
                        echo "APP_PORT=${APP_PORT}" > .env
                    """

                    // Display the contents of the workspace for debugging
                    sh 'pwd && ls -la'

                    // Stop existing containers if running
                    sh """
                        docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} down || true
                    """

                    // Start the application
                    sh """
                        docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} up -d
                    """
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    // Wait for the application to be ready
                    sh """
                        # Wait for MySQL to be ready
                        timeout 300 bash -c 'until docker exec mysql-db mysqladmin ping -h localhost --silent; do sleep 5; done'

                        # Wait for the application to be ready
                        timeout 300 bash -c 'until curl -s http://localhost:${APP_PORT}/actuator/health > /dev/null; do sleep 5; done'
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Deployment successful! Application is running on port ${APP_PORT}"
        }
        failure {
            script {
                // Cleanup on failure
                sh """
                    docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} down || true
                """
                error "Deployment failed! Cleaned up resources."
            }
        }
        always {
            // Clean up unused images
            sh """
                docker image prune -f
            """
        }
    }
}