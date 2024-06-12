pipeline {
  agent {
    dockercontainer {
      image 'abhishekf5/maven-abhishek-docker-agent:v1'
      args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // mount Docker socket to access the host's Docker daemon
    }
  }


    environment {
        KUBERNETES_CREDENTIALS = credentials('kubernetes-credentials-id')
        KUBERNETES_DEPLOYMENT_NAME = 'hello-world-java-app'
    }

  stages {
    stage('Checkout') {
      steps {
        sh 'echo passed'
        //git branch: 'main', url: 'https://github.com/sonal100495/containerization-project'
      }
    }
    stage('Build and Push Docker Image') {
      environment {
        DOCKER_IMAGE = 'sonal10/hello-world-java'
        // DOCKERFILE_LOCATION = "java-maven-sonar-argocd-helm-k8s/spring-boot-app/Dockerfile"
        REGISTRY_CREDENTIALS = credentials('docker-hub-credentials-id')
      }
      steps {
        script {
            sh 'docker build -t ${DOCKER_IMAGE} .'
            def dockerImage = docker.image("${DOCKER_IMAGE}")
            docker.withRegistry('https://hub.docker.com/', "docker-hub-credentials-id'") {
                dockerImage.push()
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
