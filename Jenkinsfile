pipeline {
    agent any
    stages {
                 stage('Build Docker Image') {
                            steps {
                                script {
                                    sh '''
                                         curl -u admin:hesoyam -O \
                                                        http://192.168.33.10:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-20250105.142846-3.jar
                                                        DOCKER_BUILDKIT=1 docker build -t devops-app:latest .
                                    '''

                                    // Build Docker image
                                    sh 'docker build -t devops-app:latest .'
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
