pipeline {
    agent any
    
    environment {
        // Define Docker Hub credentials
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials-id')
        // Define Kubernetes credentials
        KUBERNETES_CREDENTIALS = credentials('kubernetes-credentials-id')
        // Define Docker image name
        DOCKER_IMAGE_NAME = 'your-docker-username/your-image-name'
        // Define Kubernetes deployment name
        KUBERNETES_DEPLOYMENT_NAME = 'your-deployment-name'
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Checkout source code from repository
                git 'https://github.com/your-repo.git'
            }
        }
        
        stage('Build and Package') {
            steps {
                // Build Java code using Maven
                sh 'mvn clean package'
            }
        }
        
        stage('Docker Build') {
            steps {
                // Build Docker image
                script {
                    docker.build(DOCKER_IMAGE_NAME)
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                // Login to Docker Hub
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_HUB_CREDENTIALS) {
                        // Push Docker image to Docker Hub
                        docker.image(DOCKER_IMAGE_NAME).push()
                    }
                }
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                // Deploy Docker image to Kubernetes cluster
                script {
                    kubernetesDeploy(
                        kubeconfigId: 'your-kubeconfig-id',
                        configs: 'your-kubeconfig-file',
                        enableConfigSubstitution: true,
                        configsInline: '''apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${KUBERNETES_DEPLOYMENT_NAME}
  labels:
    app: ${KUBERNETES_DEPLOYMENT_NAME}
spec:
  replicas: 3
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
        - containerPort: 8080''' // Define your container port here
                    )
                }
            }
        }
    }
}
