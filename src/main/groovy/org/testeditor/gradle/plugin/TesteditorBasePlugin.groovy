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
            def testEditorLanguageVersion = verifyTestEditorLanguageVersionIsSet()
            configureXtextVersion(testEditorLanguageVersion)
            configureGsonVersion()
            configureGradleToolingVersion()
            configureCommonsLangVersion()
            addDependencies(testEditorLanguageVersion)
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
                    fileExtension = 'aml'
                    setup = 'org.testeditor.aml.dsl.AmlStandaloneSetup'
                    generator.outlet.producesJava = true
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

    private def String verifyTestEditorLanguageVersionIsSet() {
        def testEditorLanguageVersion = project.testeditor.languageVersion
        if (!testEditorLanguageVersion) {
            throw new InvalidUserDataException("""
                Test-editor language version was not specified!

                Add the following to your build script:
                testeditor {
                    languageVersion 'x.x.x'
                }
            """.stripIndent())
        }
        return testEditorLanguageVersion
    }

    // TODO it would be nicer to extract the Xtext languageVersion by analyzing the dependencies
    private def void configureXtextVersion(String testEditorLanguageVersion) {
        if (project.testeditor.xtextVersion) {
            xtext.version = project.testeditor.xtextVersion
        } else {
            xtext.version = "2.15.0"
        }
    }

    private def void configureGsonVersion() {
        if (!project.testeditor.gsonVersion) {
            project.testeditor.gsonVersion = "2.8.5"
        }
    }

    private def void configureGradleToolingVersion() {
        if (!project.testeditor.gradleToolingVersion) {
            project.testeditor.gradleToolingVersion = "4.3"
        }
    }

    private def void configureCommonsLangVersion() {
        if (!project.testeditor.commonsLangVersion) {
            project.testeditor.commonsLangVersion = "3.4"
        }
    }

    private def void addDependencies(String testEditorLanguageVersion) {
        project.dependencies.with {
            // required since 1.6.0
            add('xtextLanguages', "com.google.code.gson:gson:${this.project.testeditor.gsonVersion}")
            // required since 1.2.0
            add('xtextLanguages', "org.apache.commons:commons-lang3:${this.project.testeditor.commonsLangVersion}")
            add('xtextLanguages', "org.gradle:gradle-tooling-api:${this.project.testeditor.gradleToolingVersion}")
            add('xtextLanguages', "org.testeditor:org.testeditor.dsl.common.model:$testEditorLanguageVersion")
            // required for all versions
            add('xtextLanguages', "org.testeditor:org.testeditor.dsl.common:$testEditorLanguageVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.aml.model:$testEditorLanguageVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.aml.dsl:$testEditorLanguageVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.tsl.model:$testEditorLanguageVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.tsl.dsl:$testEditorLanguageVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.tcl.model:$testEditorLanguageVersion")
            add('xtextLanguages', "org.testeditor:org.testeditor.tcl.dsl:$testEditorLanguageVersion")

            // TODO we use xtend.core for its JDT dependencies only
            add('xtextLanguages', "org.eclipse.xtend:org.eclipse.xtend.core:${xtext.version}")

            // TODO check if we can already resolve org.junit.*
            add('testCompile', 'junit:junit:4.12')
        }
    }

}
