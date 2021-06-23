import hudson.model.Job
import jenkins.model.Jenkins

for (job in Jenkins.instance.getAllItems(Job.class)) {
    if (job.disabled) {
        println ""
        println "Job Name:     " + job.fullName
        println "Deleting."
        job.delete()
    }
}
