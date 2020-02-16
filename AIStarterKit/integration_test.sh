#!/usr/bin/env bash
./gradlew AIStarterKit:fatJar
cat AIStarterKit/src/test/resources/gamestates.txt | java -jar AIStarterKit/build/libs/AIStarterKit.jar --json-pass-through