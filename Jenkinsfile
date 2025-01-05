pipeline {
    agent any
    environment {
            APP_PORT = '8080'
        }
    stages {

                 stage('Cleanup Previous Deployment') {
                             steps {
                                 script {
                                     sh '''
                                         # Stop any existing docker compose services
                                         if docker compose ps | grep -q "devops-app"; then
                                             docker compose down
                                         fi

                                         # Find and kill any process using port 8080
                                         PORT_PID=$(lsof -ti:8080)
                                         if [ ! -z "$PORT_PID" ]; then
                                             echo "Killing process using port 8080: $PORT_PID"
                                             kill -9 $PORT_PID || true
                                         fi

                                         # Remove any stopped containers
                                         docker container prune -f
                                     '''
                                 }
                             }
                         }
                         stage('Deploy with Docker Compose') {
                             steps {
                                 script {
                                     // Start the application with Docker Compose
                                     sh '''
                                         # Export port as environment variable
                                         export APP_PORT=${APP_PORT}

                                         # Start containers
                                         docker compose up -d

                                         # Wait for application to start
                                         echo "Waiting for application to start..."
                                         for i in {1..30}; do
                                             if curl -s http://localhost:${APP_PORT} > /dev/null; then
                                                 echo "Application is up and running!"
                                                 exit 0
                                             fi
                                             sleep 2
                                             echo "Attempt $i: Application not ready yet..."
                                         done

                                         echo "Application failed to start!"
                                         docker compose logs app
                                         exit 1
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
