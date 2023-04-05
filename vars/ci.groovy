def call() {
  if (!env.sonar_extra_opts) {
    env.sonar_extra_opts=""
  }
  pipeline {
    agent any

    stages {

      stage('Compile/Build') {
        steps {
          script {
            withAWSParameterStore(credentialsId: '', naming: 'absolute', path: '/sonarqube/user', recursive: true, regionName: 'us-east-1') {
             sh 'env'
              sh 'exit 1'
            }
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

      stage('Code Quality') {
        steps {
          script {
            common.codequality()
          }
        }
      }
    }

    post {
      failure {
        mail body: "<h1>${component} - Pipeline Failed \n ${BUILD_URL}</h1>", from: 'raghudevopsb71@gmail.com', subject: "${component} - Pipeline Failed", to: 'raghudevopsb71@gmail.com',  mimeType: 'text/html'
      }
    }

  }
}
