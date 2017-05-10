package org.testeditor.gradle.plugin

class SourceSetPathsIntegrationTest extends AbstractIntegrationTest {

    def "output of source set runs successfully"() {
        when:
        def result = runTasksSuccessfully("sourceSetPaths")

        then: "source sets are printed to the console"
        result.standardOutput.with {
            find(/sourceSetPath: '.*src[\/\\]main[\/\\]java'/)
            find(/sourceSetPath: '.*src[\/\\]test[\/\\]java'/)
        }
    }

}
