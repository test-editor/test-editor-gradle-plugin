package org.testeditor.gradle.plugin

import nebula.test.IntegrationSpec

/**
 * Abstract base class for integration tests.
 */
abstract class AbstractIntegrationTest extends IntegrationSpec {

    String testEditorVersion = "2.0.5"

    def setup() {
        // TODO remove mavenLocal() below once the artifacts for aml, tcl, tsl are released
        buildFile << """
            buildscript {
                dependencies {
                    classpath files('../../../classes/main')
                }
            }

            apply plugin: ${TesteditorPlugin.name}

            repositories {
                jcenter()
                maven { url "http://dl.bintray.com/test-editor/maven" }
            }

            dependencies {
                testCompile 'org.testeditor.fixture:core-fixture:4.2.+'
            }

            testeditor {
                languageVersion '$testEditorVersion'
            }
        """.stripIndent()
    }

}
