def call(Map params = [:]) {
    // ----------------------------
    // Required and optional parameters
    // ----------------------------
    def imageName = params.imageName ?: error("🚨 Docker image name is required for Trivy scan")
    def imageTag = params.imageTag ?: 'latest'
    def severity = params.severity ?: 'CRITICAL,HIGH,MEDIUM' // Trivy severity levels to fail on
    def ignoreUnfixed = params.ignoreUnfixed ?: false
    def format = params.format ?: 'table' // json/table/sarif
    def exitCodeOnVuln = params.exitCodeOnVuln ?: 1
    def environment = params.environment ?: 'dev'

    def fullImage = "${imageName}:${imageTag}"
    def ignoreFlag = ignoreUnfixed ? '--ignore-unfixed' : ''


    echo "Running Trivy vulnerability scan on ${fullImage} for environment: ${environment.toUpperCase()}"

    try {
        
        sh "trivy image --exit-code ${exitCodeOnVuln} --severity ${severity} ${ignoreFlag} --format ${format} ${fullImage}"

        echo "Trivy scan completed successfully for ${fullImage} in ${environment.toUpperCase()}"
    } catch (err) {
        echo " Trivy scan failed or found vulnerabilities in ${fullImage}: ${err.getMessage()}"
        error("Stopping pipeline due to Trivy scan failure")
    }
}
