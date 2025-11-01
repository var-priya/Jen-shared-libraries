def call(Map config = [:]) {
    // Required and optional parameters
    def imageName = config.imageName ?: error(" Image name is required")
    def imageTag = config.imageTag ?: 'latest'
    def environment = config.environment ?: 'dev'
    def credentials = config.credentials ?: 'docker-hub-credentials'

    echo "Pushing Docker image ${imageName}:${imageTag} for ${environment.toUpperCase()}"

    try {
        // Wrap credentials block
        withCredentials([usernamePassword(
            credentialsId: credentials, 
            usernameVariable: 'DOCKER_USERNAME', 
            passwordVariable: 'DOCKER_PASSWORD'
        )]) {
            sh """
                echo "Checking image list before tagging..."
                docker images | grep ${imageName} || true

                echo "Tagging ${imageName}:${imageTag} as latest..."
                docker tag ${imageName}:${imageTag} ${imageName}:latest || echo "Tag failed, image not found"

                echo "Listing Docker images after tagging..."
                docker images | grep ${imageName} || true

                echo "\$DOCKER_PASSWORD" | docker login -u "\$DOCKER_USERNAME" --password-stdin

                docker push ${imageName}:${imageTag}
                docker push ${imageName}:latest
            """
        }

        echo "Docker image ${imageName}:${imageTag} successfully pushed for ${environment.toUpperCase()}"
    } catch (err) {
        echo " Failed to push Docker image for ${environment.toUpperCase()}: ${err.getMessage()}"
        error("Stopping pipeline as image was not pushed")
    }
}
