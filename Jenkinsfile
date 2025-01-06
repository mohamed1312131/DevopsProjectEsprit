pipeline {
    agent any

    environment {
        ARTIFACT_ID = "devops"
        GROUP_ID = "com.example"
        VERSION = "0.0.1-SNAPSHOT"
        NEXUS_URL = "http://192.168.50.4:8081/repository/maven-snapshots/"
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
                    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh '''
                        mvn deploy -DaltDeploymentRepository=nexus::default::${NEXUS_URL} \
                                    -Dusername=${NEXUS_USERNAME} \
                                    -Dpassword=${NEXUS_PASSWORD}
                        '''
                    }
                }
            }

            stage('Prometheus Metrics Collection') {
                steps {
                    sh 'curl -X POST ${PROMETHEUS_SERVER:-http://localhost:9090}/api/v1/admin/tsdb/snapshot'
                }
            }

                    stage('Notify Grafana') {
                        steps {
                            sh '''
                            curl -X POST \
                            -H "Content-Type: application/json" \
                            -d '{"title": "Jenkins Pipeline Completed", "message": "The pipeline execution has finished successfully."}' \
                            http://<GRAFANA_SERVER>:3000/api/annotations
                            '''
                        }
                    }
                }
    }
