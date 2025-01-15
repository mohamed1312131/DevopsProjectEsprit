pipeline {
    agent any

    stages {


        stage('Docker Build') {
                    steps {
                        script {
                            echo "Building Docker image..."
                            sh """
                                docker build -t devops:0.0.1-SNAPSHOT
                            """
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
