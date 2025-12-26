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
        success {
            echo 'Pipeline SUCCESS'
        }
        failure {
            echo 'Pipeline FAILED'
        }
    }
}
