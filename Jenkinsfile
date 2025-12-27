pipeline {
    agent any

    environment {
        MVN_HOME = tool name: 'Maven 3.9.12', type: 'maven'
    }

    stages {
        stage('Initial Cleanup') {
            steps {
                echo "Stopping and removing any existing containers"
                node {
                    powershell 'docker compose down --remove-orphans || Write-Host "No containers to remove"'
                }
            }
        }

        stage('Start Docker API') {
            steps {
                echo "Starting Docker Compose services"
                node {
                    powershell 'docker compose up -d'

                    echo "Waiting for API to be ready..."
                    powershell '''
                    $maxRetries = 10
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
        }

        stage('Run Maven Tests') {
            steps {
                echo "Running Maven tests"
                node {
                    powershell "${env.MVN_HOME}\\bin\\mvn clean test"
                }
            }
        }

        stage('Final Cleanup') {
            steps {
                echo "Stopping Docker Compose services after tests"
                node {
                    powershell 'docker compose down --remove-orphans || Write-Host "No containers to remove"'
                }
            }
        }
    }

    post {
        success {
            echo "Build and tests succeeded!"
        }

        failure {
            echo "Build or tests failed!"
        }
    }
}
