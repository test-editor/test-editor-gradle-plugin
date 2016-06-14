# test-editor-gradle-plugin

[![Build Status](https://travis-ci.org/test-editor/test-editor-gradle-plugin.svg?branch=master)](https://travis-ci.org/test-editor/test-editor-gradle-plugin)
[![codecov.io](https://codecov.io/github/test-editor/test-editor-gradle-plugin/coverage.svg?branch=master)](https://codecov.io/github/test-editor/test-editor-gradle-plugin?branch=master)

# Usage
The plugin is included in the [central plugin repository](https://plugins.gradle.org/plugin/org.testeditor.gradle-plugin). 

A minimal `build.gradle` looks like this:

	plugins {
		id 'org.testeditor.gradle-plugin' version '0.2'
	}
	
	repositories {
		jcenter()
	}
	
The version of the test-editor can be configured as follows:

	testeditor {
	    version '1.0.0'
	}

You can also configure the versions of the languages individually:

	testeditor {
		amlVersion '1.0.0'
		tmlVersion '1.0.0'
		tclVersion '1.0.0'
		tslVersion '1.0.0'
	}
