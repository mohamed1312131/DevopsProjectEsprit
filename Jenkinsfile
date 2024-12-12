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
                // Clean and compile the project
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                // Run tests and collect the test results
                sh 'mvn test'
                junit 'target/surefire-reports/*.xml' // Collect test results
            }
        }

        stage('JaCoCo Coverage Report') {
            steps {
                // Generate and verify the code coverage report
                sh 'mvn verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Run SonarQube analysis
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    // Define paths for the artifact and POM files
                    def artifactPath = "target/${ARTIFACT_ID}-${VERSION}.jar"
                    def pomPath = "target/${ARTIFACT_ID}-${VERSION}.pom"

                    // Check if the artifact and POM files exist before attempting deployment
                    if (fileExists(artifactPath) && fileExists(pomPath)) {
                        // Deploy the artifact to Nexus repository
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
                    } else {
                        error "Artifact or POM file not found. Deployment failed."
                    }
                }
            }
        }
    }

    post {
        always {
            // Print the branch name after every build
            echo "Building branch: ${env.BRANCH_NAME}"
        }
        success {
            // Notify if the build is successful
            echo 'Build succeeded!'
        }
        failure {
            // Notify if the build fails
            echo 'Build failed!'
        }
    }
}
