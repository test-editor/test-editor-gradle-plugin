package org.testeditor.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 */
class TesteditorPlugin implements Plugin<Project> {

    static final String NAME = "org.testeditor.gradle-plugin"

    @Override
    void apply(Project project) {
        // Register extension
        project.extensions.create(TesteditorPluginExtension.NAME, TesteditorPluginExtension)
    }

}
