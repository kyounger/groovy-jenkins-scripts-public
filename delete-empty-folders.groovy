import com.cloudbees.hudson.plugins.folder.Folder
import jenkins.model.Jenkins

println "Finding empty folders ..."

def numDeleted

while (numDeleted == null || numDeleted > 0) {
    numDeleted = 0

    for (folder in Jenkins.instance.getAllItems(Folder.class)) {
        if (folder.getItems().size() == 0) {
            println ""
            println "Folder is empty: " + folder.getFullName()
            println "Deleting."
            folder.delete()
            numDeleted++
        }
    }

    println "numDeleted: " + numDeleted
}
