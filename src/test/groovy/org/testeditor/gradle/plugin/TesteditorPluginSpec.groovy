package org.testeditor.gradle.plugin

import nebula.test.PluginProjectSpec
import org.gradle.api.InvalidUserDataException

/**
 * Unit tests for {@link TesteditorPlugin}.
 */
class TesteditorPluginSpec extends PluginProjectSpec {

    @Override
    String getPluginName() {
        return TesteditorPlugin.NAME
    }

    private def void applyWithTesteditorVersion(String version) {
        project.apply plugin: pluginName
        project.testeditor.version = version
    }

    def "proper message is thrown if test editor version was not set"() {
        given:
        applyWithTesteditorVersion(null)

        when:
        project.evaluate()

        then:
        def ex = thrown(RuntimeException)
        ex.cause instanceof InvalidUserDataException
    }

    def "Xtext 2.10.0 is used with test-editor 1.5.0"() {
        given:
        applyWithTesteditorVersion("1.5.0")

        when:
        project.evaluate()

        then:
        project.xtext.version == "2.10.0"
    }

    def "Xtext 2.11.0 is used with test-editor 1.6.0"() {
        given:
        applyWithTesteditorVersion("1.6.0")

        when:
        project.evaluate()

        then:
        project.xtext.version == "2.11.0"
    }

    def "custom xtextVersion has precedence"() {
        given:
        applyWithTesteditorVersion("1.6.0")
        project.testeditor.xtextVersion = "custom"

        when:
        project.evaluate()

        then:
        project.xtext.version == "custom"
    }

}
