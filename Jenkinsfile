pipeline {
    agent any
    environment {
        APP_PORT = '8089'
        VM_IP = '192.168.33.10'
    }
    stages {
       /* stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Run Tests') {
            steps {
                sh 'mvn test'
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage('JaCoCo Coverage Report') {
            steps {
                sh 'mvn verify'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Maven Build') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh '''
                    curl -u admin:hesoyam -O \
                    http://${VM_IP}:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-20250105.142846-3.jar
                    docker build -t devops-app:latest .
                '''
            }
        } */
        stage('Deploy with Docker Compose') {
            steps {
                sh '''
                    docker compose down || true
                    docker compose up -d
                '''
            }
        }
    }
    post {
        always {
            echo "Building branch: ${env.BRANCH_NAME}"
        }
        success {
            echo "Application deployed successfully at http://${VM_IP}:${APP_PORT}"
        }
        failure {
            echo 'Build failed!'
        }
    }
}
