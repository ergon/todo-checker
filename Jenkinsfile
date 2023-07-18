#!groovy

def jdk(String cmd) {
    final String javaBuildstack = 'adoptopenjdk11:514-b26094d-22'
    return "buildstack run \
    -e BUILD_NUMBER=${env.BUILD_NUMBER} \
    -e GRADLE_USER_HOME=/workspace/gradlehome \
    -e BUILD_NUMBER \
    -e ORG_GRADLE_PROJECT_repositoryArtifactoryUsername \
    -e ORG_GRADLE_PROJECT_repositoryArtifactoryApiKey \
    '$cmd' \
    ${javaBuildstack}"
}

pipeline {

	agent {
		label 'sw.os.linux && sw.tool.docker'
	}

	environment {
		ORG_GRADLE_PROJECT_repositoryArtifactoryUsername = credentials('ergon.cop.cd.artifacts-publishing-user')
		ORG_GRADLE_PROJECT_repositoryArtifactoryApiKey = credentials('ergon.cop.cd.artifacts-publishing-token')
	}

	options {
		disableConcurrentBuilds()
		buildDiscarder(logRotator(numToKeepStr: '30'))
		ansiColor('xterm')
		timestamps()
	}

	stages {
		stage('Build') {
			steps {
				sh jdk("./build.sh")
			}
		}

		stage('Publish') {
			when {
				branch 'main'
			}

			steps {
				sh jdk("./publish.sh")
			}
		}
	}
}
