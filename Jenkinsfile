pipeline {
  agent {
    docker {
      image 'abhishekf5/maven-abhishek-docker-agent:v1'
      args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // mount Docker socket to access the host's Docker daemon
    }
  }

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials-id')
        KUBERNETES_CREDENTIALS = credentials('kubernetes-credentials-id')
        DOCKER_IMAGE_NAME = 'sonal10/hello-world-java'
        KUBERNETES_DEPLOYMENT_NAME = 'hello-world-java-app'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sonal100495/containerization-project'
            }
        }
        
        stage('Build and Package') {
            steps {
                script {
                    docker.image('maven:3.6.3-jdk-8').inside {
                        sh 'mvn clean package'
                    } 
                }            
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    docker.build(DOCKER_IMAGE_NAME)
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://hub.docker.com/', DOCKER_HUB_CREDENTIALS) {
                        docker.image(DOCKER_IMAGE_NAME).push()
                    }
                }
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    kubernetesDeploy(
                        kubeconfigId: 'kubernetes-credentials-id',
                        configs: 'config',
                        enableConfigSubstitution: true,
                        configsInline: '''apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${KUBERNETES_DEPLOYMENT_NAME}
  labels:
    app: ${KUBERNETES_DEPLOYMENT_NAME}
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ${KUBERNETES_DEPLOYMENT_NAME}
  template:
    metadata:
      labels:
        app: ${KUBERNETES_DEPLOYMENT_NAME}
    spec:
      containers:
      - name: ${KUBERNETES_DEPLOYMENT_NAME}
        image: ${DOCKER_IMAGE_NAME}
        ports:
        - containerPort: 8080'''
                    )
                }
            }
        }
    }
}
