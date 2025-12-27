pipeline {
    agent any

    environment {
        MVN_HOME = tool(name: 'Maven 3', type: 'maven')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }
		
		stage('Cleanup') {
            steps {
                echo "Stopping and removing old containers"
                bat 'docker compose down --remove-orphans || true'
            }
        }

        stage('Start API (Docker)') {
            steps {
                echo 'Starting API with Docker Compose'
                bat 'docker compose up -d'
            }
        }

        stage('Wait for API') {
            steps {
                echo 'Waiting for API to be ready'
                bat 'timeout /t 10'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running Maven tests'
                bat "\"${MVN_HOME}\\bin\\mvn\" clean test"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            echo 'Stopping Docker containers'
            bat 'docker compose down'
        }
        success {
            echo 'Pipeline SUCCESS'
        }
        failure {
            echo 'Pipeline FAILED'
        }
    }
}
