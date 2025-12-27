pipeline {
    agent any
    environment {
        MVN_HOME = '"C:\\Program Files\\Apache\\maven-3.9.12"'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }
    stages {

        stage('Clean Workspace') {
            steps {
                echo 'Cleaning workspace...'
                deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Start Docker API') {
            steps {
                echo 'Starting Docker Compose services...'
                powershell '''
                docker compose down --remove-orphans
                docker compose up -d
                '''
            }
        }

        stage('Wait for API') {
            steps {
                echo 'Waiting for API to be ready...'
                powershell '''
                $maxRetries = 15
                $apiReady = $false
                for ($i=0; $i -lt $maxRetries; $i++) {
                    try {
                        $response = Invoke-WebRequest -Uri http://127.0.0.1:3000/posts -UseBasicParsing -TimeoutSec 2
                        if ($response.StatusCode -eq 200) {
                            Write-Host "API is ready!"
                            $apiReady = $true
                            break
                        }
                    } catch {
                        Write-Host "Waiting for API... retry $($i+1)/$maxRetries"
                        Start-Sleep -Seconds 2
                    }
                }
                if (-not $apiReady) {
                    throw "API did not start in time"
                }
                '''
            }
        }

        stage('Run Maven Tests') {
            steps {
                echo 'Running Maven tests with retry...'
                retry(3) {
                    powershell "${env.MVN_HOME}\\bin\\mvn clean test -Dsurefire.printSummary=true"
                }
            }
            post {
                always {
                    echo 'Archiving JUnit results...'
                    junit 'target/surefire-reports/*.xml'
                }
                unsuccessful {
                    echo 'Tests failed! Marking build as FAILURE'
                    script { currentBuild.result = 'FAILURE' }
                }
            }
        }

    }

    post {
        always {
            echo 'Stopping Docker Compose services after tests...'
            powershell '''
            docker compose down --remove-orphans
            '''
        }
        failure {
            echo 'Build failed!'
        }
        success {
            echo 'Build succeeded!'
        }
    }
}
