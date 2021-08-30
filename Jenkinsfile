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
                    sh "./mvnw clean javadoc:jar deploy -U -B -P sonatype-oss-release -s /private/jenkins/settings.xml"
                } 
            }
        }
    }
}
