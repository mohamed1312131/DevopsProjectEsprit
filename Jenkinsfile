pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKERHUB_USERNAME = 'mohamedsalahmechergui'
        IMAGE_NAME = 'devops-app'
        IMAGE_VERSION = '0.0.1-20250105.142846-3'
    }

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    sh '''
                        # Tag the existing local image for DockerHub
                        docker tag devops-app:latest ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest
                        docker tag devops-app:latest ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_VERSION}
                    '''
                }
            }
        }

        stage('Login to DockerHub') {
            steps {
                script {
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    sh '''
                        docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest
                        docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_VERSION}
                    '''
                }
            }
        }

        stage('Clean Up') {
            steps {
                script {
                    sh '''
                        docker rmi ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest || true
                        docker rmi ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_VERSION} || true
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed"
        }
        success {
            echo "Successfully pushed to DockerHub: ${DOCKERHUB_USERNAME}/${IMAGE_NAME}"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}