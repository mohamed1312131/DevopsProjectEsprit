pipeline {
    agent any
    environment {
        APP_PORT = '8089'
        VM_IP = '192.168.33.10'
        // Updated with your DockerHub credentials ID
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        // Your DockerHub username
        DOCKERHUB_USERNAME = 'mohamedsalahmechergui'
        // Specific version from Nexus
        ARTIFACT_VERSION = '0.0.1-20250105.142846-3'
        IMAGE_NAME = 'devops-app'
    }
    stages {
        stage('Prepare Workspace') {
            steps {
                script {
                    sh '''
                        rm -rf deployment
                        mkdir -p deployment
                        cp Dockerfile deployment/
                        cp docker-compose.yml deployment/
                    '''
                }
            }
        }

        stage('Download Artifact from Nexus') {
            steps {
                script {
                    sh '''
                        cd deployment
                        # Download the specific version from Nexus
                        curl -u admin:hesoyam -O \
                        "http://${VM_IP}:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-${ARTIFACT_VERSION}.jar"

                        # Verify the file exists
                        if [ ! -f "devops-${ARTIFACT_VERSION}.jar" ]; then
                            echo "Failed to download JAR from Nexus"
                            exit 1
                        fi
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh '''
                        cd deployment
                        # Build the Docker image with multiple tags
                        docker build -t ${IMAGE_NAME}:latest \
                                   -t ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest \
                                   -t ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${ARTIFACT_VERSION} .
                    '''
                }
            }
        }

        stage('Login to DockerHub') {
            steps {
                script {
                    sh '''
                        echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                    '''
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    sh '''
                        # Push both latest and versioned tags
                        docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest
                        docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${ARTIFACT_VERSION}
                    '''
                }
            }
        }

        stage('Clean Up') {
            steps {
                script {
                    sh '''
                        # Remove local images to save space
                        docker rmi ${IMAGE_NAME}:latest || true
                        docker rmi ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest || true
                        docker rmi ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${ARTIFACT_VERSION} || true
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed"
            cleanWs()
        }
        success {
            echo "Successfully built and pushed to DockerHub: ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${ARTIFACT_VERSION}"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}