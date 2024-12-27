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
              script {
                  currentBuild.description = "See build metrics in Grafana: <a href='${env.GRAFANA_URL}'>Grafana Dashboard</a>"
              }
              emailext(
                  to: 'aziz.lachkar45@gmail.com',
                  subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                  body: """
                  <p>The build succeeded!</p>
                  <p>Job: <a href="${env.BUILD_URL}">${env.JOB_NAME} #${env.BUILD_NUMBER}</a></p>
                  <p>See build metrics in Grafana: <a href='${env.GRAFANA_URL}'>Grafana Dashboard</a></p>
                  """,
                  mimeType: 'text/html'
              )
          }
          failure {
              echo 'Build failed!'
              script {
                  currentBuild.description = "See build metrics in Grafana: <a href='${env.GRAFANA_URL}'>Grafana Dashboard</a>"
              }
              emailext(
                  to: 'aziz.lachkar45@gmail.com',
                  subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                  body: """
                  <p>The build failed!</p>
                  <p>Job: <a href="${env.BUILD_URL}">${env.JOB_NAME} #${env.BUILD_NUMBER}</a></p>
                  <p>See build metrics in Grafana: <a href='${env.GRAFANA_URL}'>Grafana Dashboard</a></p>
                  """,
                  mimeType: 'text/html'
              )
          }
      }
  }

}
