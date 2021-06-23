import hudson.model.Job
import jenkins.model.Jenkins

def days = 365
def cutOffTime = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * days

//useful override to test "older than 5 seconds"; just uncomment
//cutOffTime = System.currentTimeMillis() - 1000L * 5

println ">> BATCH DETAILS"
println ">> CutOffTime in Millis:  " + cutOffTime
println ""

println "Finding jobs older than ${days} days..."
for (job in Jenkins.instance.getAllItems(Job.class)) {
    if(!job.disabled) {

        def build = job.getLastSuccessfulBuild()

        if (build != null && build.getTimeInMillis() < cutOffTime) {
            println ""
            println "Job Name:     " + job.fullName
            println "Build Number: " + build.number
            println "Job Last Run: " + build.timestampString2
            println "Disabling."

            job.disabled = true
        }
    }
}
