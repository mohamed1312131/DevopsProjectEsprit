pipeline {
    agent any
    environment {
        APP_PORT = '8089'
        VM_IP = '192.168.33.10'
    }
    stages {
        /*stage('Build') {
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
        stage("mvn build") {
            steps {
                script {
                    sh "mvn package -DskipTests"
                }
            }
        }
        stage('Deploy to Nexus') {
            steps {
                script {
                    sh "mvn clean deploy -DskipTests"
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh '''
                        mkdir -p deployment
                        cd deployment
                        curl -u admin:hesoyam -O \
                        http://${VM_IP}:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-20250105.142846-3.jar
                        docker build -t devops-app:latest .
                    '''
                }
            }
        } */
        stage('Deploy Containers') {
            steps {
                script {
                    sh '''
                        cd deployment
                        export APP_PORT=${APP_PORT}
                        docker compose down --remove-orphans
                        docker compose up -d
                        docker compose ps
                    '''
                }
            }
        }
    }
    post {
        always {
            echo "Building branch: ${env.BRANCH_NAME}"
        }
        success {
            echo "Application deployed successfully at http://${env.VM_IP}:${env.APP_PORT}"
        }
        failure {
            echo 'Build failed!'
            script {
                sh '''
                    cd deployment
                    if docker compose ps | grep -q "devops-app"; then
                        docker compose logs app
                        docker compose down
                    fi
                '''
            }
        }
    }
}