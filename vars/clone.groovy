def call(Map Config = [:]){
  def repoUrl = config.url ?: 'https://github.com/var-priya/E-commerce-application.git'
  def branchName = config.branchName ?: 'master'
  def credentialsId = config.credentialsId ?: ''
  def environment = config.environment ?: 'dev'

  try{
    echo "Checking out code for ${environment.toUpperCase()} from ${repoUrl} (branch: ${branchName})"
    git url: repoUrl, branch: branchName, credentialsId: credentialsId
    echo "Checkout successfull for ${environment.toUpperCase()}"
  }
  catch(err){
    echo "Failed to Checkout for ${environment.toUpperCase()} : ${err.getMessage()}"
    error("Stopping pipeline due to failure Checkout for ${envorinment.toUpperCase()})
  }
  
