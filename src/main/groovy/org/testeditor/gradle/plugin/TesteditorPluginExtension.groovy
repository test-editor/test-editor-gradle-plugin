package org.testeditor.gradle.plugin

/**
 * Configuration for the plugin.
 */
class TesteditorPluginExtension {

    static final String NAME = "testeditor"

    /** The version of the languages to use. */
    def String version = "1.0.0-SNAPSHOT" // TODO we don't want a default value

    /** The version of AML to use, defaults to {@link #version}. */
    def String amlVersion = version

    /** The version of TCL to use, defaults to {@link #version}. */
    def String tclVersion = version

    /** The version of TSL to use, defaults to {@link #version}. */
    def String tslVersion = version

}
