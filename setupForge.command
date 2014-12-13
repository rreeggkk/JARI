#!/bin/bash
cd "$( dirname "$0" )"

echo setupDecompWorkspace
./gradlew setupDecompWorkspace

echo eclipse
./gradlew eclipse