pipeline {
    agent any
    stages {

                 stage('Deploy with Docker Compose') {
                             steps {
                                 script {
                                     // Stop any existing containers
                                     sh '''
                                         if docker compose ps | grep -q "devops-app"; then
                                             docker compose down
                                         fi
                                     '''

                                     // Start the application with Docker Compose using the file from Git
                                     sh 'docker compose up -d'

                                     // Wait for application to be ready
                                     sh '''
                                         echo "Waiting for application to start..."
                                         sleep 30
                                         if curl -s http://localhost:8080 > /dev/null; then
                                             echo "Application is up and running!"
                                         else
                                             echo "Application failed to start!"
                                             docker compose logs app
                                             exit 1
                                         fi
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
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
