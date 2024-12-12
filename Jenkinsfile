pipeline {
    agent any

    environment {
        NEXUS_URL = 'http://192.168.50.4:8081/repository/maven-snapshots/'
        NEXUS_CREDENTIALS = 'nexus' // Jenkins credentials ID
        GROUP_ID = 'com.example'
        ARTIFACT_ID = 'devops'
        VERSION = '0.0.1-SNAPSHOT'
    }

    sh 'echo $NEXUS_USERNAME'
     sh 'echo $NEXUS_PASSWORD'


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
                    def artifactPath = "target/${ARTIFACT_ID}-${VERSION}.jar"
                    def pomPath = "target/${ARTIFACT_ID}-${VERSION}.pom"

                    // Use credentials from Jenkins
                    withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIALS}",
                                                       usernameVariable: 'NEXUS_USERNAME',
                                                       passwordVariable: 'NEXUS_PASSWORD')]) {
                        if (fileExists(artifactPath) && fileExists(pomPath)) {
                            sh """
                            mvn deploy:deploy-file -Dfile=${artifactPath} \
                                                   -DpomFile=${pomPath} \
                                                   -DrepositoryId=nexus \
                                                   -Durl=${NEXUS_URL} \
                                                   -DgroupId=${GROUP_ID} \
                                                   -DartifactId=${ARTIFACT_ID} \
                                                   -Dversion=${VERSION} \
                                                   -Dpackaging=jar \
                                                   -Dusername=\${NEXUS_USERNAME} \
                                                   -Dpassword=\${NEXUS_PASSWORD}
                            """
                        } else {
                            sh """
                            mvn deploy:deploy-file -Dfile=${artifactPath} \
                                                   -DrepositoryId=nexus \
                                                   -Durl=${NEXUS_URL} \
                                                   -DgroupId=${GROUP_ID} \
                                                   -DartifactId=${ARTIFACT_ID} \
                                                   -Dversion=${VERSION} \
                                                   -Dpackaging=jar \
                                                   -Dusername=\${NEXUS_USERNAME} \
                                                   -Dpassword=\${NEXUS_PASSWORD}
                            """
                        }
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
        }
        failure {
            echo 'Build failed!'
        }
    }
}
