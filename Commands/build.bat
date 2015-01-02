cd "$( dirname "$0" )"
cd ../

gradlew.bat clean

gradlew.bat build

gradlew.bat copyNativesLegacy