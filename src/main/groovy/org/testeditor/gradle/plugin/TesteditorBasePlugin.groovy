package org.testeditor.gradle.plugin

import org.gradle.api.InvalidUserDataException
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
            def testeditorVersion = verifyTesteditorVersionIsSet()
            configureXtextVersion(testeditorVersion)
            addDependencies(testeditorVersion)
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

    private def String verifyTesteditorVersionIsSet() {
        def testeditorVersion = project.testeditor.version
        if (!testeditorVersion) {
            throw new InvalidUserDataException("""
                Test-editor version was not specified!

                Add the following to your build script:
                testeditor {
                    version 'x.x.x'
                }
            """.stripIndent())
        }
        return testeditorVersion
    }

    // TODO it would be nicer to extract the Xtext version by analyzing the dependencies
    private def void configureXtextVersion(String testEditorVersion) {
        if (project.testeditor.xtextVersion) {
            xtext.version = project.testeditor.xtextVersion
        } else {
            if (isVersionGreaterOrEquals_1_6_0(testEditorVersion)) {
                xtext.version = "2.11.0"
            } else {
                xtext.version = "2.10.0"
            }
        }
    }

    private def void addDependencies(String testEditorVersion) {
        project.dependencies.with {
            if (isVersionGreaterOrEquals_1_6_0(testEditorVersion)) {
                // required since 1.6.0
                add('xtextLanguages', "com.google.code.gson:gson:2.7")
            }
            // required since 1.2.0
            add('xtextLanguages', "org.apache.commons:commons-lang3:3.4")
            add('xtextLanguages', "org.gradle:gradle-tooling-api:2.14.1")
            add('xtextLanguages', "org.testeditor:org.testeditor.dsl.common.model:$testEditorVersion")
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

    private def boolean isVersionGreaterOrEquals_1_6_0(String testEditorVersion) {
        String[] versionSplit = testEditorVersion.split("\\.")
        int major = Integer.parseInt(versionSplit[0])
        int minor = Integer.parseInt(versionSplit[1])
        return (major == 1 && minor >= 6) || major > 1
    }

}
