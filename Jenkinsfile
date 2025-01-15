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
                    // Create .env file for docker-compose
                    sh """
                        echo "APP_PORT=${APP_PORT}" > .env
                    """

                    // Stop existing containers if running
                    sh """
                        docker-compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} down || true
                    """

                    // Start the application
                    sh """
                        docker-compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} up -d
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
                    docker-compose -f ${COMPOSE_FILE} -p ${PROJECT_NAME} down || true
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