pipeline {
    agent any

    environment {
        ARTIFACT_ID = "devops"
        GROUP_ID = "com.example"
        VERSION = "0.0.1-SNAPSHOT"
        NEXUS_URL = "http://192.168.50.4:8081/repository/maven-snapshots/"
        GRAFANA_URL = "http://192.168.50.4:3000/d/haryan-jenkins"
        CHROME_DRIVER_PATH = "/usr/local/bin/chromedriver"
        DISPLAY = ":99"  // For running headless browsers if needed (X virtual framebuffer)
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

       stage('Run Tests') {
           steps {
               // Exclude Selenium UI tests using Maven's exclude option
               script {
                          // Debugging Environment Variables with escaped dollar signs
                             echo "Debugging Environment Variables:"
                             echo "CHROME_DRIVER_PATH: \$CHROME_DRIVER_PATH"
                             echo "PATH: \$PATH"
                             echo "DISPLAY: \$DISPLAY"
                             echo "Current User: \$(whoami)" // Escape the dollar signs for shell commands

                             // Checking ChromeDriver version
                             echo "Checking ChromeDriver version:"
                             sh "chromedriver --version || { echo 'ChromeDriver not found or not executable!'; exit 1; }"

                             // Checking Google Chrome version
                             echo "Checking Google Chrome version:"
                             sh "google-chrome --version || { echo 'Google Chrome not found or not executable!'; exit 1; }"

                             // Setting up the headless environment
                             echo "Setting up headless environment..."
                             sh "export DISPLAY=:99"
                             sh "Xvfb :99 -screen 0 1024x768x24 &"
                             sh "sleep 3"

                   echo "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                   // Run all tests excluding Selenium UI tests
                   sh """
                       mvn test -Dtest=com.example.devops.UITest.FoyerUITest \
                                -Dwebdriver.chrome.driver=${env.CHROME_DRIVER_PATH} \
                                -Dselenium.headless=true
                   """
               }
               junit 'target/surefire-reports/*.xml'
           }
       }

        stage('UI Tests - Selenium') {
            steps {
                script {
                    echo "Running Selenium UI tests..."
                    sh """
                        echo "Debugging Environment Variables:"
                        echo "CHROME_DRIVER_PATH: \$CHROME_DRIVER_PATH"
                        echo "PATH: \$PATH"
                        echo "DISPLAY: \$DISPLAY"
                        echo "Current User: \$(whoami)"

                        echo "Checking ChromeDriver version:"
                        chromedriver --version || { echo "ChromeDriver not found or not executable!"; exit 1; }
                        echo "Checking Google Chrome version:"
                        google-chrome --version || { echo "Google Chrome not found or not executable!"; exit 1; }

                        echo "Setting up headless environment..."
                        export DISPLAY=:99
                        Xvfb :99 -screen 0 1024x768x24 &
                        sleep 3

                        echo "Running Maven Selenium tests..."
                        mvn test -Dtest=com.example.devops.UITest.* -Dwebdriver.chrome.driver=\$CHROME_DRIVER_PATH -Dselenium.headless=true
                    """
                }
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

        stage('Docker Build') {
            steps {
                script {
                    echo "Building Docker image..."
                    sh """
                        docker build -t ${ARTIFACT_ID}:${VERSION} .
                    """
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    echo "Logging into Docker..."
                    withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                            echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin || exit 1
                            docker images || exit 1
                            docker tag ${ARTIFACT_ID}:${VERSION} ${DOCKER_USERNAME}/${ARTIFACT_ID}:${VERSION} || exit 1
                            docker push ${DOCKER_USERNAME}/${ARTIFACT_ID}:${VERSION} || exit 1
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
