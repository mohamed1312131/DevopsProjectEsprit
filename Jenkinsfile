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

        stage('Start Prometheus and Grafana') {
            steps {
                echo "Starting Prometheus and Grafana containers..."

                // Stop and remove existing containers safely
                sh '''
                docker stop prometheus grafana || true
                docker rm prometheus grafana || true
                '''

                // Check if the prometheus.yml file exists using Bash syntax
                sh '''
                if [ -f "/path/to/prometheus.yml" ]; then
                    docker run -d --name prometheus \
                    -p 9090:9090 \
                    -v /path/to/prometheus.yml:/etc/prometheus/prometheus.yml \
                    prom/prometheus
                else
                    echo "Error: prometheus.yml not found!"
                    exit 1
                fi
                '''

                // Start Grafana
                sh '''
                docker run -d --name grafana \
                -p 3000:3000 \
                grafana/grafana
                '''
            }
        }



    }

    post {
        always {
            echo "Building branch: ${env.BRANCH_NAME}"
        }
        success {
            echo 'Build succeeded!'

            // Cleanup containers
            sh '''
            docker stop prometheus grafana || true
            docker rm prometheus grafana || true
            '''
        }
        failure {
            echo 'Build failed! Cleaning up monitoring containers...'
            sh '''
            docker stop prometheus grafana || true
            docker rm prometheus grafana || true
            '''
        }
    }
}
