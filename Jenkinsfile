pipeline {
    agent any

    environment {
        NEXUS_URL = 'http://192.168.50.4:8081/repository/maven-snapshots/'
        NEXUS_CREDENTIALS = 'nexus-credentials-id' // Replace with the actual Jenkins credentials ID
        GROUP_ID = 'com.example'
        ARTIFACT_ID = 'devops'
        VERSION = '0.0.1-SNAPSHOT'
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
                    // Deploy artifact to Nexus repository
                    def artifactPath = "target/${ARTIFACT_ID}-${VERSION}.jar"
                    def pomPath = "target/${ARTIFACT_ID}-${VERSION}.pom"

                    // Upload .jar and .pom to Nexus
                    sh """
                    mvn deploy:deploy-file -Dfile=${artifactPath} \
                                           -DpomFile=${pomPath} \
                                           -DrepositoryId=nexus \
                                           -Durl=${NEXUS_URL} \
                                           -DgroupId=${GROUP_ID} \
                                           -DartifactId=${ARTIFACT_ID} \
                                           -Dversion=${VERSION} \
                                           -Dpackaging=jar
                    """
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
