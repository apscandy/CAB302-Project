/* Requires the Docker Pipeline plugin */
pipeline {
    agent { docker { image 'maven:3.9.9-amazoncorretto-21' } }
    stages {
        /*This stage is to be added later */
        stage('testing') {
            steps {
                sh 'mvn --version'
            }
        }
        stage('build') {
            steps {
                sh 'mvn -B --file pom.xml'
            }
        }
        /*This stage is to be added later */
        stage('generate docs') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}