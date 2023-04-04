def call() {
  pipeline {
    agent any

    stages {

      stage('Compile/Build') {
        steps {
          if(app_lang == "nodejs") {
            sh 'npm install'
          }
          if(app_lang == "maven") {
            sh 'mvn package'
          }
        }
      }

      stage('Test Cases') {
        steps {
          echo 'Test Cases'
        }
      }

    }

  }
}
