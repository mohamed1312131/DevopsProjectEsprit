pipeline {
    agent any

    stages {
    stage{
        steps {
                script {
                    sh '''
                        curl -u admin:hesoyam -O \
                        http://192.168.33.10:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-20250105.142846-3.jar
                        DOCKER_BUILDKIT=1 docker build -t devops-app:latest .
                    '''
                }
            }
    }
        stage('Build Docker Image') {
            steps {
                script {
                    // Debug: Verify the artifact exists before building the Docker image
                    sh 'ls -l devops-0.0.1-20250105.142846-3.jar'

                    // Build the Docker image
                    sh 'docker build -t devops-app:latest .'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs for details.'
        }
    }
}