pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        MVN_HOME = tool name 'Maven 3', type 'maven'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Start Docker Environment') {
            steps {
                echo Launch of Docker Compose...
                sh docker-compose -f ${DOCKER_COMPOSE_FILE} up -d
                sh sleep 5
            }
        }

        stage('Run Tests') {
            steps {
                echo Exécution Maven Tests...
                sh ${MVN_HOME}binmvn clean test
            }
            post {
                always {
                    junit 'targetsurefire-reports.xml'
                }
            }
        }

        stage('Stop Docker Environment') {
            steps {
                echo Stop and Clean Docker Container...
                sh docker-compose -f ${DOCKER_COMPOSE_FILE} down
            }
        }
    }

    post {
        success {
            echo Pipeline successfullly finished ✅
        }
        failure {
            echo Pipeline failed ❌
        }
    }
}