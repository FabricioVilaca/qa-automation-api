pipeline {
    agent any

    tools {
        maven 'Maven 3'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Start API') {
            steps {
                echo 'Starting API with Docker Compose'
                powershell '''
                    docker compose down
                    docker compose up -d
                '''
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running Maven tests'
                powershell 'mvn clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            echo 'Stopping API'
            powershell 'docker compose down'
        }
    }
}
