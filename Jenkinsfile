pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'JDK21'
    }

    environment {
        SONAR_PROJECT_KEY = 'devops-app'
        SONAR_PROJECT_NAME = 'DevOps Java Application'
    }

    stages {
        // ÉTAPE 1: Checkout GitHub
        stage('Checkout GitHub') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/baderrss/devops-app.git',
                    credentialsId: 'github-credentials'
                sh 'echo "✅ Étape 1/6 - Code récupéré depuis GitHub"'
            }
        }

        // ÉTAPE 2: Build Maven
        stage('Build Maven') {
            steps {
                sh 'mvn clean compile'
                sh 'echo "✅ Étape 2/6 - Application compilée"'
            }
        }

        // ÉTAPE 3: Tests JUnit
        stage('Tests Unitaires') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'echo "✅ Étape 3/6 - Tests unitaires exécutés"'
                }
            }
        }

        // ÉTAPE 4: SAST - SonarQube
        stage('SAST - SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh """
                        echo "🔍 Étape 4/6 - Analyse SonarQube en cours..."
                        mvn sonar:sonar \
                          -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                          -Dsonar.projectName=${SONAR_PROJECT_NAME} \
                          -Dsonar.host.url=http://localhost:9000 \
                          -Dsonar.java.binaries=target/classes \
                          -Dsonar.sources=src/main/java \
                          -Dsonar.tests=src/test/java
                    """
                }
            }
        }

        // ÉTAPE 5: Quality Gate
        stage('Quality Gate') {
            steps {
                echo "📊 Étape 5/6 - Vérification Quality Gate..."
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                sh 'echo "✅ Quality Gate passée"'
            }
        }

        // ÉTAPE 6: Déploiement Tomcat
        stage('Deploy Tomcat') {
            steps {
                sh '''
                    echo "🚀 Étape 6/6 - Déploiement Tomcat..."
                    mvn package -DskipTests
                    curl -u admin:admin123 \
                         -T "target/devops-app.war" \
                         "http://localhost:8081/manager/text/deploy?path=/devops-app&update=true"
                    echo "✅ Application déployée sur Tomcat"
                '''
            }
        }
    }

    post {
        always {
            echo "📊 === RAPPORT FINAL ==="
            echo "🌐 SonarQube: http://localhost:9000/dashboard?id=devops-app"
            echo "🚀 Application: http://localhost:8081/devops-app/hello"
        }
        success {
            echo "🎉 PIPELINE RÉUSSI - Toutes les étapes validées!"
        }
        failure {
            echo "❌ PIPELINE EN ÉCHEC - Vérifiez les logs"
        }
    }
}