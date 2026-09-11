pipeline {
    agent any

    options {
        disableConcurrentBuilds()
    }

    environment {
        DOCKER_IMAGE = 'bhushitha16/calculator'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
        EMAIL_TO = 'bhushitha16@gmail.com'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh """
                    docker build \
                        -t ${DOCKER_IMAGE}:${DOCKER_TAG} \
                        .
                """
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin

                        docker push ${DOCKER_IMAGE}:${DOCKER_TAG}

                        docker logout
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                sh """
                    docker compose down
                    IMAGE_TAG=${DOCKER_TAG} docker compose up -d
                """
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    echo "Waiting for application to become healthy..."

                    for i in $(seq 1 30); do
                        if curl -fs http://localhost/actuator/health; then
                            echo ""
                            echo "Application is healthy."
                            exit 0
                        fi

                        echo "Waiting..."
                        sleep 5
                    done

                    echo "Application failed health check."
                    exit 1
                '''
            }
        }
    }

    post {

        success {
            emailext(
                to: "${EMAIL_TO}",
                subject: "SUCCESS: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
Build successful.

Job: ${JOB_NAME}
Build: #${BUILD_NUMBER}
Status: SUCCESS

Docker image:
${DOCKER_IMAGE}:${DOCKER_TAG}

The application was successfully deployed using Docker Compose.
"""
            )
        }

        failure {
            emailext(
                to: "${EMAIL_TO}",
                subject: "FAILURE: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
Build failed.

Job: ${JOB_NAME}
Build: #${BUILD_NUMBER}
Status: FAILURE

Please check the Jenkins console output for details.
"""
            )
        }
    }
}