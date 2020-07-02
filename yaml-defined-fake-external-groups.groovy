//only run on OC

import io.fabric8.kubernetes.client.utils.Serialization
import jenkins.model.Jenkins
import nectar.plugins.rbac.groups.Group
import nectar.plugins.rbac.groups.GroupContainerLocator

// can read this yaml from file. Make this part of a pipeline, etc.
String userGroupsYaml = """
groups:
- name: teamA
  users:
    - User001
- name: teamB
  users:
    - User002
    - User003
"""

Jenkins jenkins = Jenkins.getInstance();
def container = GroupContainerLocator.locate(jenkins);

// code to delete all groups that start with ext-
//def groups = container.getGroups().findAll { it.name.startsWith("ext-") }
//groups.each {
//    container.deleteGroup(it)
//}

def yamlReader = Serialization.yamlMapper()
Map map = yamlReader.readValue(userGroupsYaml, Map.class);

//map.groups.each { group ->
//    println(group.name)
//    group.users.each { user ->
//        println(user)
//    }
//}

map.groups.each { group ->
    String fakeExternalGroupName = "ext-${group.name}";

    println("externalGroupName: ${fakeExternalGroupName}");
    def existingFakeExternalGroup = container.getGroups().find{it.name==fakeExternalGroupName}

    // ensure our fake "external group" is created and all REAL external users are the only members
    if(!existingFakeExternalGroup) {
        existingFakeExternalGroup = new Group(container, fakeExternalGroupName);
        existingFakeExternalGroup.setMembers(group.users)
        container.addGroup(existingFakeExternalGroup);
    } else {
        existingFakeExternalGroup.setMembers(group.users)
    }

    sleep(500)
}

