def call(Map config = [:]) {
    def imageName = config.imageName ?: error("Image name is required")
    def imageTag = config.imageTag ?: 'latest'
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context = config.context ?: '.'
    def environment = config.environment ?: 'dev'
    
    echo "Building Docker image for ${environment.toUpperCase()} : ${imageName}:${imageTag} using ${dockerfile}"
    try{
    sh """
        docker build -t ${imageName}:${imageTag} -f ${dockerfile} ${context}
    """
    echo "Docker image ${imageName}:${imageTag} is successfully build for ${environment.toUpperCase()} from ${dockerfile}"
    }
    catch(err){
        echo "Failed to build the image for ${environment.toUpperCase()} : ${err.getMessage()}"
        error("Stopping pipeline as  build is failed ")
    }
}
