package org.testeditor.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.xtext.gradle.XtextBuilderPlugin
import org.xtext.gradle.tasks.XtextExtension

class TesteditorBasePlugin implements Plugin<Project> {

    Project project
    XtextExtension xtext
    TesteditorPluginExtension config

    @Override
    void apply(Project project) {
        this.project = project

        // Apply required plugins
        project.apply {
            plugin(JavaBasePlugin)
            plugin(XtextBuilderPlugin)
        }

        // Retrieve configurations
        config = project.extensions.create(TesteditorPluginExtension.NAME, TesteditorPluginExtension)
        xtext = project.extensions.getByType(XtextExtension)

        configureXtextPlugin()

        project.afterEvaluate {
            addDependencies()
        }

        createSourceSetPathsTask(project)
    }

    private def void createSourceSetPathsTask(Project target) {
        SourceSetPathsTask sourceSetPathsTask = target.getTasks().create("sourceSetPaths", SourceSetPathsTask.class);
        sourceSetPathsTask.setDescription("Displays all java source set paths");
        sourceSetPathsTask.setGroup("Help");
    }

    private def void configureXtextPlugin() {
        xtext.with {
            version = config.xtextVersion
            languages {
                aml {
                    setup = 'org.testeditor.aml.dsl.AmlStandaloneSetup'
                }
                tsl {
                    setup = 'org.testeditor.tsl.dsl.TslStandaloneSetup'
                }

                tcl {
                    fileExtension = 'tcl'
                    setup = 'org.testeditor.tcl.dsl.TclStandaloneSetup'
                    generator.outlet.producesJava = true
                }

                if (isVersionSmaller_1_2_0(config.version)) {
                    // required prior to 1.2.0 - TML was unified with TCL in 1.2.0
                    tml {
                        setup = 'org.testeditor.tml.dsl.TmlStandaloneSetup'
                    }
                } else {
                    // TODO this is quite ugly, need this until https://github.com/xtext/xtext-gradle-plugin/issues/71 is resolved
                    tclMacro {
                        fileExtension = 'tml'
                        setup = 'org.testeditor.tcl.dsl.TclStandaloneSetup'
                        generator.outlet.producesJava = true
                    }
                    tclConfig {
                        fileExtension = 'config'
                        setup = 'org.testeditor.tcl.dsl.TclStandaloneSetup'
                        generator.outlet.producesJava = true
                    }
                }
            }
        }
    }

    private def void addDependencies() {
        def testEditorVersion = project.testeditor.version
        project.dependencies.with {
            if (isVersionGreaterOrEquals_1_2_0(testEditorVersion)) {
                // required since 1.2.0
                add('xtextLanguages', "org.apache.commons:commons-lang3:3.4")
                add('xtextLanguages', "org.gradle:gradle-tooling-api:2.14.1")
                add('xtextLanguages', "org.testeditor:org.testeditor.dsl.common.model:$testEditorVersion")
            }
            if (isVersionSmaller_1_2_0(testEditorVersion)) {
                // required prior to 1.2.0 - TML was unified with TCL in 1.2.0
                add('xtextLanguages', "org.testeditor:org.testeditor.tml.model:$testEditorVersion")
                add('xtextLanguages', "org.testeditor:org.testeditor.tml.dsl:$testEditorVersion")
            }
            // required for all versions
            add('xtextLanguages', "org.testeditor:org.testeditor.dsl.common:$testEditorVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.aml.model:$testEditorVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.aml.dsl:$testEditorVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.tsl.model:$testEditorVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.tsl.dsl:$testEditorVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.tcl.model:$testEditorVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.tcl.dsl:$testEditorVersion")

            // TODO we use xtend.core for its JDT dependencies only
            add('xtextLanguages', "org.eclipse.xtend:org.eclipse.xtend.core:${xtext.version}")

            // TODO check if we can already resolve org.junit.*
            add('testCompile', 'junit:junit:4.12')
        }
    }

    private def boolean isVersionGreaterOrEquals_1_2_0(String testEditorVersion) {
        String[] versionSplit = testEditorVersion.split("\\.")
        int major = Integer.parseInt(versionSplit[0])
        int minor = Integer.parseInt(versionSplit[1])
        return (major == 1 && minor >= 2) || major > 1
    }

    private def boolean isVersionSmaller_1_2_0(String testEditorVersion) {
        String[] versionSplit = testEditorVersion.split("\\.")
        int major = Integer.parseInt(versionSplit[0])
        int minor = Integer.parseInt(versionSplit[1])
        return major == 1 && minor < 2
    }

}
