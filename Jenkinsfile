pipeline {
    agent {
        docker {
            image 'maven:3.8.5-openjdk-17'
        }
    }

    environment {
        ARTIFACT_ID = "devops"
        GROUP_ID = "com.example"
        VERSION = "0.0.1-SNAPSHOT"
        NEXUS_URL = "http://192.168.50.4:8081/repository/maven-snapshots/"
        PROMETHEUS_SERVER = "http://localhost:9090"
        GRAFANA_SERVER = "http://localhost:3000"
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

        stage('Quality Gate Check') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh '''
                    mvn deploy -DaltDeploymentRepository=nexus::default::${NEXUS_URL}
                    '''
                }
            }
        }

        stage('Prometheus Metrics Collection') {
            steps {
                script {
                    try {
                        sh 'curl -X POST ${PROMETHEUS_SERVER}/api/v1/admin/tsdb/snapshot'
                    } catch (Exception e) {
                        echo "Failed to notify Prometheus: ${e}"
                    }
                }
            }
        }

        stage('Notify Grafana') {
            steps {
                script {
                    try {
                        sh '''
                        curl -X POST ${GRAFANA_SERVER}/api/annotations \
                        -H "Content-Type: application/json" \
                        -d '{
                            "text": "Build Completed",
                            "tags": ["jenkins", "build"]
                        }'
                        '''
                    } catch (Exception e) {
                        echo "Failed to notify Grafana: ${e}"
                    }
                }
            }
        }
    }
}
