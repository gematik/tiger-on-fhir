@Library('gematik-jenkins-shared-library') _

def CREDENTIAL_ID_GEMATIK_GIT = 'svc_gitlab_prod_credentials'
def REPO_URL = createGitUrl('git/Testtools/tiger/tiger-on-fhir')
def BRANCH = 'main'
def JIRA_PROJECT_ID = 'TGRFHIR'
def GITLAB_PROJECT_ID = '1174'

pipeline {
    options {
        disableConcurrentBuilds()
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')
    }
    agent { label 'k8-maven-small' }

    stages {
        stage('Initialize') {
            steps {
                useJdk('OPENJDK17')
                gitSetIdentity()
            }
        }

        stage('Code format check') {
            steps {
                script {
                    try {
                        sh """
                           mvn spotless:check
                           """
                    } catch (err) {
                        unstable(message: "${STAGE_NAME} is unstable")
                    }
                }
            }
        }
    }
}
