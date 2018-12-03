pipeline {
  agent any
  stages {
    stage('compile') {
      agent any
      steps {
        sh '''mvn clean install
'''
      }
    }
  }
}