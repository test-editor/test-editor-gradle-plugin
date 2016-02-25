package org.testeditor.gradle.plugin

import nebula.test.PluginProjectSpec

/**
 * Unit tests for {@link TesteditorPlugin}.
 */
class TesteditorPluginSpec extends PluginProjectSpec {

    @Override
    String getPluginName() {
        return TesteditorPlugin.NAME
    }

}
