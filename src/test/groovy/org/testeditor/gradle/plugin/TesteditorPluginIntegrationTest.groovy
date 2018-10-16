package org.testeditor.gradle.plugin
/**
 * Integration tests for {@link TesteditorPlugin}.
 */
class TesteditorPluginIntegrationTest extends AbstractIntegrationTest {

    def "can generate empty test case"() {
        given:
        createTestCase()

        when:
        runTasksSuccessfully("generateTestXtext")

        then:
        getGeneratedFile("Example").exists()
    }

    def "can run empty test case"() {
        given:
        createTestCase()

        when:
        runTasksSuccessfully("build")

        then: "assert class got compiled"
        new File(projectDir, "build/classes/java/test/com/example/Example.class").exists()

        and: "test got executed"
        def testResult = new File(projectDir, "build/test-results/test/TEST-com.example.Example.xml")
        def suite = new XmlSlurper().parse(testResult)
        suite.@name == "com.example.Example"
        suite.@tests == "1"
        suite.@skipped == "0"
        suite.@failures == "0"
        suite.@errors == "0"
    }

    def "can generate test case with macro"() {
        given:
        createMacroCollection("""
            ## SampleMacro
            template = "Nothing"
        """)
        createTestCase("""
            * step one
                Macro: MyMacroCollection
                - Nothing
        """)

        when:
        runTasksSuccessfully("generateTestXtext")

        then:
        def generatedFile = getGeneratedFile("Example")
        generatedFile.exists()
        generatedFile.text.contains("macro_MyMacroCollection_SampleMacro();")
    }

    def "can generate test case with configuration"() {
        given:
        createTestConfiguration()
        createTestCase("""
            config MyConfig
        """)

        when:
        runTasksSuccessfully("generateTestXtext")

        then:
        def generatedFile = getGeneratedFile("Example")
        generatedFile.exists()
        generatedFile.text.contains("public class Example extends MyConfig {")
    }

    def "gradle clean removes generated artifact"() {
        given:
        createTestCase()
        runTasksSuccessfully("generateTestXtext")
        assert getGeneratedFile("Example").exists()

        when:
        runTasksSuccessfully("clean")

        then:
        !getGeneratedFile("Example").exists()
    }

    private File getGeneratedFile(String tclName) {
        return new File(projectDir, "build/tclMacro/test/com/example/${tclName}.java")
    }

    private void createTestCase(String contents = "") {
        def file = createFile("src/test/java/com/example/Example.tcl")
        file << """
            package com.example

            # Example

            $contents
        """.stripIndent()
    }

    private void createMacroCollection(String contents = "") {
        def file = createFile("src/test/java/com/example/MyMacroCollection.tml")
        file << """
            package com.example

            # MyMacroCollection

            $contents
        """.stripIndent()
    }

    private void createTestConfiguration(String contents = "") {
        def file = createFile("src/test/java/com/example/MyConfig.config")
        file << """
            package com.example

            config MyConfig

            $contents
        """.stripIndent()
    }

}
