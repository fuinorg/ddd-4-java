pipeline {
    agent any 
    tools { 
        jdk 'OpenJDK 11 (latest)'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh "./mvnw -version"
            }
        }
        stage('Build') { 
            steps {
                withCredentials( [ 
                        string(credentialsId: 'sonar_login', variable: 'SONAR_LOGIN') 
                                 ] ) {
                    sh "./mvnw clean javadoc:jar deploy jacoco:report sonar:sonar -U -B -P sonatype-oss-release -s /private/jenkins/settings.xml -Dsonar.organization=fuinorg -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=${SONAR_LOGIN}"
                } 
            }
        }
    }
}
