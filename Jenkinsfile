pipeline {
    agent any

    environment {
        ARTIFACT_ID = "devops"
        GROUP_ID = "com.example"
        VERSION = "0.0.1-SNAPSHOT"
        NEXUS_URL = "http://192.168.50.4:8081/repository/maven-snapshots/"
        GRAFANA_URL = "http://192.168.50.4:3000/d/haryan-jenkins" // Update with your Grafana dashboard URL
    }

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
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh """
                            mvn deploy -DaltDeploymentRepository=nexus::default::${NEXUS_URL} \
                                        -Dusername=${NEXUS_USERNAME} \
                                        -Dpassword=${NEXUS_PASSWORD}
                        """
                    }
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
            // Add Grafana dashboard link to build description
            script {
                currentBuild.description = "See build metrics in Grafana: <a href='${env.GRAFANA_URL}'>Grafana Dashboard</a>"
            }
        }
        failure {
            echo 'Build failed!'
            // Add Grafana dashboard link to build description
            script {
                currentBuild.description = "See build metrics in Grafana: <a href='${env.GRAFANA_URL}'>Grafana Dashboard</a>"
            }
        }
    }

}
