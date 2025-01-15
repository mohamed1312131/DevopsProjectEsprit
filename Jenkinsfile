pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'mohamedsalahmechergui'
        IMAGE_NAME = 'devops-app'
        IMAGE_VERSION = '0.0.1-20250105.142846-3'
        APP_PORT = '8089'
        COMPOSE_FILE = 'docker-compose.yml'
        PROJECT_NAME = 'devops'
        MYSQL_CONTAINER = "mysql-db-${BUILD_NUMBER}"
        APP_CONTAINER = "devops-app-${BUILD_NUMBER}"
        APP_URL = "http://192.168.33.10:8089"
    }

    stages {
        stage('Cleanup Previous Deployment') {
            steps {
                script {
                    sh '''
                        docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} down || true
                        docker rm -f mysql-db devops-app || true
                        docker network rm devops_devops-network || true
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
                    sh """
                        echo "APP_PORT=${APP_PORT}" > .env
                        docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} up -d
                    """
                }
            }
        }

        stage('Verify Web Access') {
            steps {
                script {
                    sh """
                        # Wait for MySQL and application to be ready
                        echo "Waiting for MySQL to be ready..."
                        timeout 300 bash -c 'until docker exec ${MYSQL_CONTAINER} mysqladmin ping -h localhost --silent; do sleep 5; done'

                        echo "Waiting for application to be accessible..."
                        timeout 300 bash -c 'until curl -s ${APP_URL} > /dev/null; do sleep 5; echo "Waiting for application to respond..."; done'

                        echo "Application is now accessible at ${APP_URL}"
                        echo "You can now access the following endpoints:"
                        echo "- Main URL: ${APP_URL}"
                        echo "- Bloc API: ${APP_URL}/bloc/findAll"

                        # Get response from main URL
                        echo "Testing main URL response:"
                        curl -v ${APP_URL}
                    """
                }
            }
        }
    }

    post {
        success {
            echo """
                Deployment successful!

                Your application is now running!
                Access it at: ${APP_URL}
                Test the API at: ${APP_URL}/bloc/findAll

                You can now open these URLs in your web browser.
            """
        }
        failure {
            script {
                echo "Deployment failed! Cleaning up..."
                sh """
                    docker compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} down || true
                    docker rm -f ${MYSQL_CONTAINER} ${APP_CONTAINER} || true
                """
            }
        }
        always {
            sh "docker image prune -f"
        }
    }
}