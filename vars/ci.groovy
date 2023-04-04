def call() {
  pipeline {
    agent any

    stages {

      stage('Compile/Build') {
        steps {
          echo 'Compile/Build'
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
