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
        project.testeditor.languageVersion = version
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

    def "Xtext 2.15.0 (default) is used with test-editor 2.0.5"() {
        given:
        applyWithTesteditorVersion("2.0.5")

        when:
        project.evaluate()

        then:
        project.xtext.version == "2.15.0"
    }

    def "custom xtextVersion has precedence"() {
        given:
        applyWithTesteditorVersion("1.13.0")
        project.testeditor.xtextVersion = "custom"

        when:
        project.evaluate()

        then:
        project.xtext.version == "custom"
    }

}
