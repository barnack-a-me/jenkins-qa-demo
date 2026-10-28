pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 10, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    // Revisa el repositorio cada minuto; si hay un commit nuevo (git push) dispara el pipeline
    triggers {
        pollSCM('* * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compilar') {
            steps {
                sh 'mvn -B -ntp clean compile'
            }
        }

        stage('Pruebas Unitarias') {
            steps {
                sh 'mvn -B -ntp test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Cobertura (JaCoCo)') {
            steps {
                sh 'mvn -B -ntp jacoco:check'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
                }
            }
        }

        stage('Empaquetar') {
            steps {
                sh 'mvn -B -ntp package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Desplegar (simulado)') {
            steps {
                echo 'Todas las pruebas pasaron. Desplegando version 1.0.0 (simulado)...'
            }
        }
    }

    post {
        success {
            echo 'PIPELINE EXITOSO: el codigo esta listo para entregar.'
        }
        failure {
            echo 'PIPELINE FALLIDO: se detuvo el despliegue. Revisar Test Result y Console Output.'
        }
    }
}
