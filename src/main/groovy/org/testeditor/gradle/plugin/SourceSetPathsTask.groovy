package org.testeditor.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class SourceSetPathsTask extends DefaultTask {

    public SourceSetPathsTask() {
        super()
        outputs.upToDateWhen { false }
    }

    @TaskAction
    def void sourceSetPaths() {
        project.sourceSets.each({
            it.allJava.getSourceDirectories().each({
                println("sourceSetPath: '$it.path'")
            })
        })
    }

}
