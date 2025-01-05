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
                            sh "mvn package -DskipTests"
                        }
                    }
        }
               stage('Deploy to Nexus') {
                    steps {
                        script {
                            sh "mvn clean deploy -DskipTests"
                        }
                    }
                }
                 stage('Build Docker Image') {
                            steps {
                                script {
                                    sh '''
                                        curl -u admin:hesoyam -O \
                                        http://192.168.33.10:8081/repository/maven-snapshots/com/example/devops/0.0.1-SNAPSHOT/devops-0.0.1-20250105.142846-3.jar
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
