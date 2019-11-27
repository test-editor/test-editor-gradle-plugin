#!/bin/bash
# Taken from https://github.com/franzbecker/gradle-lombok/blob/master/.travis-publishOnRelease.sh

# Execute only on tag builds where the tag starts with 'v'
if [[ -n "$TRAVIS_TAG" && "$TRAVIS_TAG" == v* ]]; then
    echo "Starting publishing version: $TRAVIS_TAG"
    ./gradlew bintrayUpload
fi
