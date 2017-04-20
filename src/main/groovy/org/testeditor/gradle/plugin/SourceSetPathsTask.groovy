package org.testeditor.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Project

class SourceSetPathsTask extends DefaultTask {
    @TaskAction
    def sourceSetPaths() {
        project.sourceSets.each({
            it.allJava.getSourceDirectories().each({
                println("sourceSetPath: '" + it.getPath() + "'")
            })
        })
    }
}
