#!/bin/bash
cd "$( dirname "$0" )"
cd ../

./gradlew setupDecompWorkspace

./gradlew eclipse