def call() {
  pipeline {
    agent any

    stages {

      stage('Compile/Build') {
        steps {
          script {
            common.compile()
          }
        }
      }

      stage('Test Cases') {
        steps {
          script {
            common.testcases()
          }
        }
      }

    }

  }
}
