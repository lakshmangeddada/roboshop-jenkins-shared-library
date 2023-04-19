def call() {
  pipeline {
    agent any

    options {
      ansiColor('xterm')
    }

    parameters {
      string(name: 'app_version', defaultValue: '', description: 'App Version')
      string(name: 'component', defaultValue: '', description: 'Component')
      string(name: 'environment', defaultValue: '', description: 'Environment')
    }

    stages {
      stage('Update Parameter Store') {
        steps {
          sh 'aws ssm put-parameter --name ${environment}.${component}.app_version --type "String" --value "${app_version}" --overwrite'
        }
      }
      stage('Deploy Servers') {
        steps {
          script {
            sh 'aws autoscaling start-instance-refresh --auto-scaling-group-name ${component}-${environment} --preferences \'{"InstanceWarmup": 180, "MinHealthyPercentage": 90}\''
            }
          }

        
      }
    }

    post {
      always {
        cleanWs()
      }
    }

  }
}