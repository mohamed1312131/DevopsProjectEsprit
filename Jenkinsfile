pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Starting build stage...'
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('JaCoCo Coverage Report') {
            steps {
                echo 'Generating JaCoCo coverage report...'
                sh 'mvn verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying artifacts to Nexus...'
                withCredentials([usernamePassword(credentialsId: 'nexus-repo', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        mvn deploy \
                        -DaltDeploymentRepository=nexus-repo::default::http://192.168.33.10:8081/repository/myDevopsNexusRepo/ \
                        -Dusername=$NEXUS_USERNAME \
                        -Dpassword=$NEXUS_PASSWORD
                    """
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed for branch: ${env.BRANCH_NAME}"
        }
        success {
            echo 'Build and deployment succeeded!'
        }
        failure {
            echo 'Build or deployment failed. Please check the logs.'
        }
    }
}
