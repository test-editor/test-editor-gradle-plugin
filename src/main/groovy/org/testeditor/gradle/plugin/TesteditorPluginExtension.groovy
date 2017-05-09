package org.testeditor.gradle.plugin

/**
 * Configuration for the plugin.
 */
class TesteditorPluginExtension {

    static final String NAME = "testeditor"

    /** The version of the languages to use. */
    def String version

    /** The Xtext version to use. If not set the defaults are used. */
    def String xtextVersion

}
