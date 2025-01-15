pipeline {
    agent any
    environment {
        APP_PORT = '8089'
        VM_IP = '192.168.33.10'
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building the application...'
                sh 'mvn clean compile'
            }
        }
        stage('Maven Package') {
            steps {
                echo 'Packaging the application...'
                sh 'mvn package -DskipTests'
            }
        }
        stage('Build Docker Image') {
            steps {
                echo 'Creating Docker image...'
                sh '''
                    curl -u admin:hesoyam -O \
                    http://${VM_IP}:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-20250105.142846-3.jar
                    docker build -t devops-app:latest .
                '''
            }
        }
    }
    post {
        always {
            echo "Building branch: ${env.BRANCH_NAME}"
        }
        success {
            echo "Docker image created successfully: devops-app:latest"
        }
        failure {
            echo 'Build failed!'
        }
    }
}
