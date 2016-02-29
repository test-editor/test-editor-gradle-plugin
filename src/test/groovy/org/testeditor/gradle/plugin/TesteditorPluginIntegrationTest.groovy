package org.testeditor.gradle.plugin

import spock.lang.Ignore

/**
 * Integration tests for {@link TesteditorPlugin}.
 */
@Ignore("does not work on travis until we release aml/tcl/tsl.")
class TesteditorPluginIntegrationTest extends AbstractIntegrationTest {

    def "compile simple tcl"() {
        given:
        createTclSource()

        when:
        def result = runTasksSuccessfully("build")

        then:
        def output = new File(projectDir, "build/tcl/main/com/example/Example.java")
        assert output
    }

    private void createTclSource() {
        def file = createFile("src/main/java/com/example/Example.tcl")
        file << """
            package com.example

            # Example
        """.stripIndent()
    }

}
