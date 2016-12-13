package org.testeditor.gradle.plugin

import nebula.test.IntegrationSpec

/**
 * Abstract base class for integration tests.
 */
abstract class AbstractIntegrationTest extends IntegrationSpec {

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
                testCompile 'org.testeditor.fixture:core-fixture:3.1.0'
            }
        """.stripIndent()
    }

}
