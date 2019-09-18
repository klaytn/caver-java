#!/bin/bash

#take version from build.gradle
if [ -f build.gradle ]; then
    version=$(awk '/version '\''/ {gsub("'\''",""); print $2}' build.gradle)
    echo $version
else
    echo "build.gradle not exist"
fi