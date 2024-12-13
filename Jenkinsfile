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
        stage("mvn build") {
                    steps {
                        script {
                            sh "mvn package"
                        }
                    }
        }
               stage('Deploy to Nexus') {
                    steps {
                        script {
                            sh "mvn deploy"
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
