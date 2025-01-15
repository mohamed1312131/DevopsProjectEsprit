pipeline {
    agent any
    stages {
        stage('Fetch Snapshot from Nexus') {
            steps {
                sh '''
                    curl -u admin:hesoyam -O \
                    http://192.168.33.10:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-20250105.142846-3.jar
                '''
            }
        }
        stage('Build Docker Image') {
            steps {
                sh '''
                    ls -l
                    docker build -t devops-app:latest .
                '''
            }
        }
    }
}
