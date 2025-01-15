pipeline {
    agent any

    stages {

        stage('Download Artifact') {
            steps {
                script {
                    // Download the artifact from Nexus
                    sh '''
                        curl -u admin:hesoyam -O \
                        http://192.168.33.10:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-20250105.142846-3.jar
                    '''
                }
            }
        }
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
