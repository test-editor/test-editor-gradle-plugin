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

        // Configure Xtext plugin
        xtext.with {
            version = config.xtextVersion
            languages {
                aml {
                    setup = 'org.testeditor.aml.dsl.AmlStandaloneSetup'
                }
                tml {
                    setup = 'org.testeditor.tml.dsl.TmlStandaloneSetup'
                }
                tcl {
                    setup = 'org.testeditor.tcl.dsl.TclStandaloneSetup'
                    generator.outlet.producesJava = true
                }
                tsl {
                    setup = 'org.testeditor.tsl.dsl.TslStandaloneSetup'
                }
            }
        }

        // Add dependencies
        // TODO we could improve this by not using the common xtextLanguages
        project.afterEvaluate {
            def testEditorVersion = project.testeditor.version
            def amlVersion = project.testeditor.amlVersion ?: testEditorVersion
            def tmlVersion = project.testeditor.tmlVersion ?: testEditorVersion
            def tclVersion = project.testeditor.tclVersion ?: testEditorVersion
            def tslVersion = project.testeditor.tslVersion ?: testEditorVersion
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.dsl.common:${testEditorVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.aml.model:${amlVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.aml.dsl:${amlVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tml.model:${tmlVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tml.dsl:${tmlVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tcl.model:${tclVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tcl.dsl:${tclVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tsl.model:${tslVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tsl.dsl:${tslVersion}")

            // TODO we use xtend.core for its JDT dependencies only
            project.dependencies.add('xtextLanguages', "org.eclipse.xtend:org.eclipse.xtend.core:${xtext.version}")

            // TODO check if we can already resolve org.junit.*
            project.dependencies.add('testCompile', 'junit:junit:4.12')
        }
    }

}
