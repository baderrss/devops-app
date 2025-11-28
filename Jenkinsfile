pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'JDK21'
    }

    stages {
        stage('Checkout GitHub') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/baderrss/devops-app.git',
                    credentialsId: 'github-credentials'
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Tests Unitaires') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SAST - SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=devops-app'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Deploy Tomcat') {
            steps {
                sh '''
                    mvn package -DskipTests
                    curl -u admin:admin123 \
                         -T "target/devops-app.war" \
                         "http://localhost:8081/manager/text/deploy?path=/devops-app&update=true"
                '''
            }
        }
    }

    post {
        success {
            echo '🎉 Application déployée avec succès!'
        }
    }
}