package org.testeditor.gradle.plugin

import spock.lang.Ignore

/**
 * Integration tests for {@link TesteditorPlugin}.
 */
@Ignore("does not work on travis until we release aml/tcl/tsl.")
class TesteditorPluginIntegrationTest extends AbstractIntegrationTest {

    def "can generate empty test case"() {
        given:
        createTclSource()

        when:
        runTasksSuccessfully("generateTestXtext")

        then:
        new File(projectDir, "build/tcl/test/com/example/Example.java").exists()
    }

    def "can run empty test case"() {
        given:
        createTclSource()

        when:
        runTasksSuccessfully("build")

        then: "assert class got compiled"
        new File(projectDir, "build/classes/test/com/example/Example.class").exists()

        and: "test got executed"
        def testResult = new File(projectDir, "build/test-results/TEST-com.example.Example.xml")
        def suite = new XmlSlurper().parse(testResult)
        suite.@name == "com.example.Example"
        suite.@tests == "1"
        suite.@skipped == "0"
        suite.@failures == "0"
        suite.@errors == "0"
    }

    private void createTclSource() {
        def file = createFile("src/test/java/com/example/Example.tcl")
        file << """
            package com.example

            # Example
        """.stripIndent()
    }

}
