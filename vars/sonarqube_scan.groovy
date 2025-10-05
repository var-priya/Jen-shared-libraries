def call(Map config = [:]) {
    def projectKey = config.projectKey ?: error("Project key is required")
    def projectName = config.projectName ?: "My Project"
    def envinronment = config.envinronment ?: 'dev'
    def sonarServer = config.sonarServer ?: 'sonar'
    
    echo " Running SonarQube scan for project '${projectKey}' in '${environment.toUpperCase()}' environment"
    try{
    withSonarQubeEnv(sonarServer){
         sh "sonar-scanner -dsonar.projectKey = ${projectKey} -Dsonar.projectName = ${projectName} -Dsonar.sources = ."
    }
    echo "SonarQube scanning is completed successfully for ${environment.toUpperCase}."
    }
    catch(err){
        echo "Failed to scan for ${environment.toUpperCase} : ${err.getMessage()}"
        error("Stopping pipeline due to SonarQube scan failure ")
    }
}
