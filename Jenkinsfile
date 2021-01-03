def results = ""
pipeline{
  agent any
  tools {
    jdk 'LOCAL_JDK11'
  }
  options {
    disableConcurrentBuilds()
    timeout(time: 1, unit: "HOURS")
  }
  stages {
    stage("Restart API") {
      when { expression { BRANCH_NAME ==~ /(master|main|develop)/ }}
      steps {
        dir('./docker/') {
          sh '''
            chmod +x deploy.sh
            bash deploy.sh
          '''
        }
      }
    }
  }
  post{
    always {
      echo 'JENKINS PIPELINE'
    }

    success {
      echo 'JENKINS PIPELINE SUCCESSFUL'
    }

    failure {
      echo 'JENKINS PIPELINE FAILED'
    }

    unstable {
      echo 'JENKINS PIPELINE WAS MARKED AS UNSTABLE'
    }

    changed {
      echo 'JENKINS PIPELINE STATUS HAS CHANGED SINCE LAST EXECUTION'
    }
  }
}
