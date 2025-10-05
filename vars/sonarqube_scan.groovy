def call(Map config = [:]) {
    def projectKey = config.projectKey ?: error("Project key is required")
    def projectName = config.projectName ?: "My Project"
    def environment = config.envinronment ?: 'dev'
    def sonarServer = config.sonarServer ?: 'sonar'
    
    echo " Running SonarQube scan for project '${projectKey}' in '${environment.toUpperCase()}' environment"
    try{
    withSonarQubeEnv(sonarServer){
         sh "sonar-scanner -dsonar.projectKey = ${projectKey} -Dsonar.projectName = ${projectName} -Dsonar.sources = ."
    }
    // Wait for Quality Gate result
        timeout(time: 5, unit: 'MINUTES') {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
                error("Pipeline stopped due to Quality Gate failure: ${qg.status}")
            } else {
                echo "Quality Gate passed for ${projectKey}"
            }
        }
    }
    catch(err){
        echo "Failed to scan for ${environment.toUpperCase} : ${err.getMessage()}"
        error("Stopping pipeline due to SonarQube scan failure ")
    }
}
