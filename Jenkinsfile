pipeline {
    agent any
    environment {
        APP_PORT = '8089'
        VM_IP = '192.168.33.10'
    }
    stages {
        stage('Fetch Snapshot from Nexus') {
            steps {
                script {
                    sh '''
                        curl -u admin:hesoyam -O \
                        http://192.168.33.10:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-SNAPSHOT.jar
                    '''
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh '''
                        docker build -t devops-app:latest .
                    '''
                }
            }
        }
        stage('Push Docker Image to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                        docker tag devops-app:latest $DOCKER_USERNAME/devops-app:latest
                        docker push $DOCKER_USERNAME/devops-app:latest
                    '''
                }
            }
        }
        stage('Run Docker Compose') {
            steps {
                script {
                    sh 'docker-compose up -d'
                }
            }
        }
    }
    post {
        always {
            echo "Pipeline completed."
        }
        success {
            echo "Application is running successfully at http://${VM_IP}:${APP_PORT}"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
