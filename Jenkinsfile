pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                // Run the tests
                sh 'mvn test'
                
                junit 'target/surefire-reports/*.xml'

            }
        }
        stage('JaCoCo Coverage Report') {
                    steps {
                        // Generate JaCoCo XML report
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
