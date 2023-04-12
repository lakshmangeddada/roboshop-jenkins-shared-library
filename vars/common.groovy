def compile() {
  if(app_lang == "nodejs") {
    sh 'npm install'
  }
  if(app_lang == "maven") {
    sh 'mvn package ; mv target/${component}-1.0.jar ${component}.jar'
  }
}

def testcases() {
  // npm test
  // mvn test
  // python -m unittests
  // go test
  sh 'echo OK'
}


def codequality() {
  withAWSParameterStore(credentialsId: 'PARAM1', naming: 'absolute', path: '/sonarqube', recursive: true, regionName: 'us-east-1') {
      //sh 'sonar-scanner -Dsonar.host.url=http://172.31.12.89:9000 -Dsonar.login=${SONARQUBE_USER} -Dsonar.password=${SONARQUBE_PASS} -Dsonar.projectKey=${component} ${sonar_extra_opts} -Dsonar.qualitygate.wait=true'
      sh 'echo OK'
    }

}

def prepareArtifacts() {
  sh 'echo ${TAG_NAME} >VERSION'
  if (app_lang == "maven") {
    sh 'zip -r ${component}-${TAG_NAME}.zip ${component}.jar schema VERSION'
  } else {
    sh 'zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile'
  }

}

def artifactUpload() {
  MASKED_SECRET = 'I_SHOULD_BE_MASKED'
  MASKED_SECRET1 = 'I_SHOULD_BE_MASKED'
  wrap([$class: 'MaskPasswordsBuildWrapper',
        varPasswordPairs: [[password: MASKED_SECRET], [password: MASKED_SECRET1]]]) {
    echo 'Retrieve Secret: ' +  MASKED_SECRET
    echo 'Retrieve Secret1: ' +  MASKED_SECRET1
  }
  NEXUS_USER = sh ( script: 'aws ssm get-parameter --name prod.nexus.user --with-decryption | jq .Parameter.Value | xargs', returnStdout: true ).trim()
  sh 'echo ${TAG_NAME} >VERSION'
  //if (app_lang == "nodejs" || app_lang == "angular") {
    sh 'curl -v -u ${NEXUS_USER}:admin123 --upload-file ${component}-${TAG_NAME}.zip http://172.31.11.210:8081/repository/${component}/${component}-${TAG_NAME}.zip'
  //}

}