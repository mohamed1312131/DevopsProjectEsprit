pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'mohamedsalahmechergui'
        IMAGE_NAME = 'devops-app'
        IMAGE_VERSION = '0.0.1-20250105.142846-3'
        APP_PORT = '8089'
        COMPOSE_FILE = 'docker-compose.yml'
        PROJECT_NAME = 'devops'
        // Use Jenkins BUILD_NUMBER to create unique container names
        MYSQL_CONTAINER = "mysql-db-${BUILD_NUMBER}"
        APP_CONTAINER = "devops-app-${BUILD_NUMBER}"
    }

    stages {
        stage('Cleanup Previous Deployment') {
            steps {
                script {
                    // Stop and remove any existing containers
                    sh '''
                        # Stop any existing deployment
                        docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} down || true

                        # Remove any leftover containers
                        docker rm -f mysql-db devops-app || true

                        # Remove any existing networks
                        docker network rm devops_devops-network || true

                        # Clean up any dangling volumes
                        docker volume prune -f
                    '''
                }
            }
        }

        stage('Create Docker Compose File') {
            steps {
                script {
                    writeFile file: 'docker-compose.yml', text: '''services:
  app:
    image: devops-app:latest
    container_name: ${APP_CONTAINER}
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
    container_name: ${MYSQL_CONTAINER}
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
                    // Create .env file with dynamic container names
                    sh """
                        echo "APP_PORT=${APP_PORT}" > .env
                        echo "MYSQL_CONTAINER=${MYSQL_CONTAINER}" >> .env
                        echo "APP_CONTAINER=${APP_CONTAINER}" >> .env
                    """

                    // Start the application
                    sh """
                        docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} up -d
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
                sh """
                    docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} down || true
                    docker rm -f ${MYSQL_CONTAINER} ${APP_CONTAINER} || true
                """
                error "Deployment failed! Cleaned up resources."
            }
        }
        always {
            sh """
                docker image prune -f
            """
        }
    }
}