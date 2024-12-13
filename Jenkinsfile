pipeline {
    agent any
     environment {
        NEXUS_URL ="http://192.168.33.10:8081/"
        NEXUS_REPOSITORY="myNexusRepo"
        NEXUS_CREDENTIAL_ID = "nexus-repo"
        ARTIFACT_VERSION = "${BUILD_NUMBER}"

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
        stage("mvn build") {
                    steps {
                        script {
                            sh "mvn package"
                        }
                    }
        }
                stage("publish to nexus") {
                    steps {
                        script {
                            // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
                            pom = readMavenPom file: "pom.xml";
                            // Find built artifact under target folder
                            filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                            // Print some info from the artifact found
                            echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                            // Extract the path from the File found
                            artifactPath = filesByGlob[0].path;
                            // Assign to a boolean response verifying If the artifact name exists
                            artifactExists = fileExists artifactPath;

                            if(artifactExists) {
                                echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";

                                nexusArtifactUploader(
                                    nexusUrl: NEXUS_URL,
                                    groupId: pom.groupId,
                                    version: ARTIFACT_VERSION,
                                    repository: NEXUS_REPOSITORY,
                                    credentialsId: NEXUS_CREDENTIAL_ID,
                                    artifacts: [
                                        // Artifact generated such as .jar, .ear and .war files.
                                        [artifactId: pom.artifactId,
                                        classifier: '',
                                        file: artifactPath,
                                        type: pom.packaging]
                                    ]
                                );

                            } else {
                                error "*** File: ${artifactPath}, could not be found";
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
