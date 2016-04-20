package org.testeditor.gradle.plugin

/**
 * Configuration for the plugin.
 */
class TesteditorPluginExtension {

    static final String NAME = "testeditor"

    /** The version of the languages to use. */
    def String version = "1.0.0" // TODO we don't want a default value

    /** The version of AML to use. */
    def String amlVersion = null

    /** The version of TCL to use. */
    def String tclVersion = null

    /** The version of TSL to use. */
    def String tslVersion = null

    def String xtextVersion = "2.9.1"

}
