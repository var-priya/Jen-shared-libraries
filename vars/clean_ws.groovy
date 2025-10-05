def call(Map Config = [:]) {
    def when = config.when ?: 'pre'
    def environment = config.environment ?: 'dev'
    echo "Cleaning up workspace for ${environment.toUpperCase()}(${when})"
    try{
        cleanWs(deleteDirs: true, notFailBuild: true) 
        echo "Workspace cleaned successfully for ${environment.toUpperCase()}"
    }
    catch(err){
        echo "Failed to clean Workspace for ${environment.toUpperCase()} : ${err.getMessage()}"
    }
}
