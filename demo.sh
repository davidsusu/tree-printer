#!/bin/sh

./gradlew demo -PdemoMain=hu.webarticum.treeprinter.demo."$1" --console=plain --quiet
