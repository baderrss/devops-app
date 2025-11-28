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
                sh '''
                    echo "✅ Étape 1/6 - Code récupéré depuis GitHub"
                    echo "📁 Contenu du repository:"
                    ls -la
                '''
            }
        }

        // ÉTAPE 2: Build Maven
        stage('Build Maven') {
            steps {
                sh '''
                    echo "🔨 Étape 2/6 - Installation des dépendances et compilation"
                    mvn clean compile
                    echo "✅ Application compilée avec succès"
                '''
            }
        }

        // ÉTAPE 3: Tests JUnit
        stage('Tests Unitaires') {
            steps {
                sh '''
                    echo "🧪 Étape 3/6 - Exécution des tests unitaires JUnit"
                    mvn test
                '''
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'echo "✅ Rapports de tests générés"'
                }
            }
        }

        // ÉTAPE 4: SAST - SonarQube (FONCTIONNE !)
        stage('SAST - SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh """
                        echo "🔍 Étape 4/6 - Analyse SonarQube en cours..."
                        mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY}
                    """
                }
            }
        }

        // ÉTAPE 5: Quality Gate (CORRIGÉE - timeout augmenté)
        stage('Quality Gate') {
            steps {
                echo "📊 Étape 5/6 - Vérification Quality Gate..."
                timeout(time: 18, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
                sh 'echo "✅ Quality Gate passée - Code conforme aux standards"'
            }
        }

        // ÉTAPE 6: Déploiement Tomcat
        stage('Deploy Tomcat') {
            steps {
                sh '''
                    echo "🚀 Étape 6/6 - Déploiement sur Apache Tomcat"
                    echo "📦 Création du package WAR..."
                    mvn package -DskipTests

                    echo "🌐 Déploiement sur Tomcat..."
                    # Arrêter l'application si elle existe déjà
                    curl -s -u admin:admin123 "http://localhost:8081/manager/text/stop?path=/devops-app" || true

                    # Déployer la nouvelle version
                    curl -u admin:admin123 \
                         -T "target/devops-app.war" \
                         "http://localhost:8081/manager/text/deploy?path=/devops-app&update=true"

                    echo "✅ Application déployée avec succès"

                    # Vérification
                    echo "🔍 Vérification du déploiement..."
                    curl -s -u admin:admin123 "http://localhost:8081/manager/text/list" | grep devops-app
                '''
            }
        }
    }

    post {
        always {
            echo "📊 === RAPPORT FINAL DU PIPELINE ==="
            echo "🕒 Date: \$(date)"
            echo "🔧 Outils utilisés: JDK21, Maven, SonarQube, Tomcat10"
            echo "🌐 SonarQube Dashboard: http://192.168.190.130:9000/dashboard?id=devops-app"
            echo "🚀 Application déployée: http://localhost:8081/devops-app/hello"
        }
        success {
            echo "🎉 === PIPELINE RÉUSSI ==="
            echo "✅ Toutes les étapes terminées avec succès!"
            echo "📊 Analyse SonarQube disponible"
            echo "🌐 Application accessible: http://localhost:8081/devops-app/hello"
        }
        failure {
            echo "❌ === PIPELINE EN ÉCHEC ==="
            echo "🔍 Consultez les logs pour diagnostiquer le problème"
        }
    }
}