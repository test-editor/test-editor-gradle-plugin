package org.testeditor.gradle.plugin

import nebula.test.IntegrationSpec

/**
 * Abstract base class for integration tests.
 */
abstract class AbstractIntegrationTest extends IntegrationSpec {

    def setup() {
        buildFile << """
            apply plugin: '${TesteditorPlugin.NAME}'

            repositories {
                jcenter()
            }
        """.stripIndent()
    }

}
