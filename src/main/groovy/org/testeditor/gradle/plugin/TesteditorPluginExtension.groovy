package org.testeditor.gradle.plugin

/**
 * Configuration for the plugin.
 */
class TesteditorPluginExtension {

    static final String NAME = "testeditor"

    /** The version of the languages to use. */
    def String version = "1.2.0" // TODO we don't want a default value

    def String xtextVersion = "2.10.0"

}
