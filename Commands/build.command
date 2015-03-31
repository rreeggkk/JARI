#!/bin/bash
cd "$( dirname "$0" )"
cd ../

./gradlew clean

./gradlew build

./gradlew extractNatives