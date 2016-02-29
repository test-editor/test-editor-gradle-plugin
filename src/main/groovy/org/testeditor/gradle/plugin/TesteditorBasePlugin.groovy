package org.testeditor.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.xtext.gradle.XtextBuilderPlugin
import org.xtext.gradle.protocol.GradleInstallDebugInfoRequest
import org.xtext.gradle.tasks.Language
import org.xtext.gradle.tasks.XtextExtension
import org.xtext.gradle.tasks.XtextGenerate

class TesteditorBasePlugin implements Plugin<Project> {

    Project project
    XtextExtension xtext
    TesteditorPluginExtension config
    Language aml
    Language tcl
    Language tsl

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
        xtext.version = config.xtextVersion
        aml = xtext.languages.create('aml') {
            fileExtension = 'aml'
            setup = 'org.testeditor.aml.dsl.AmlStandaloneSetup'
        }
        tcl = xtext.languages.create('tcl') {
            fileExtension = 'tcl'
            setup = 'org.testeditor.tcl.dsl.TclStandaloneSetup'
            generator.outlet.producesJava = true
        }
        tsl = xtext.languages.create('tsl') {
            fileExtension = 'tsl'
            setup = 'org.testeditor.tsl.dsl.TslStandaloneSetup'
        }

        // Add dependencies
        // TODO we could improve this by not using the common xtextLanguages
        project.afterEvaluate {
            def amlVersion = project.testeditor.amlVersion ?: project.testeditor.version
            def tclVersion = project.testeditor.tclVersion ?: project.testeditor.version
            def tslVersion = project.testeditor.tslVersion ?: project.testeditor.version
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.aml.model:${amlVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.aml.dsl:${amlVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tcl.model:${tclVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tcl.dsl:${tclVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tsl.model:${tslVersion}")
            project.dependencies.add('xtextLanguages', "org.testeditor:org.testeditor.tsl.dsl:${tslVersion}")
            // TODO we use xtend.core for its JDT dependencies only
            project.dependencies.add('xtextLanguages', "org.eclipse.xtend:org.eclipse.xtend.core:${xtext.version}")
        }
    }

}
