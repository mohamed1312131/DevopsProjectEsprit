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
        stage('Deploy to Nexus') {
                    steps {
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
