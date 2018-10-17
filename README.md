# test-editor-gradle-plugin

[![Build Status](https://travis-ci.org/test-editor/test-editor-gradle-plugin.svg?branch=master)](https://travis-ci.org/test-editor/test-editor-gradle-plugin)
[![codecov.io](https://codecov.io/github/test-editor/test-editor-gradle-plugin/coverage.svg?branch=master)](https://codecov.io/github/test-editor/test-editor-gradle-plugin?branch=master)

# Usage
The plugin is included in the [central plugin repository](https://plugins.gradle.org/plugin/org.testeditor.gradle-plugin). 

A minimal `build.gradle` looks like this:

	plugins {
		id 'org.testeditor.gradle-plugin' version '0.9'
	}
	
	repositories {
		jcenter()
		maven { url "http://dl.bintray.com/test-editor/test-editor-maven" }
	}
	
	testeditor {
	    languageVersion '2.0.5'
	}
	
# Development

## Release process

Checkout the master branch and type

	gradlew release -Prelease.useAutomaticVersion=true

The release plugin will check that you don't have incoming or uncommitted changes, build the plugin and tag the current version accordingly. Travis will then pick up the version tag and publish the plugin.
