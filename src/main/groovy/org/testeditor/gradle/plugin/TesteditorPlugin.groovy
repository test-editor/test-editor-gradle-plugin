package org.testeditor.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 *
 */
class TesteditorPlugin implements Plugin<Project> {

    static final String NAME = "org.testeditor.gradle-plugin"

    @Override
    void apply(Project project) {
        project.apply {
            plugin(JavaPlugin)
            plugin(TesteditorBasePlugin)
        }
    }

}
