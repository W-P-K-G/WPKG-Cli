#!/bin/sh

mkdir -p target
echo "Downloading jre..."
wget https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.4.1%2B1/OpenJDK17U-jre_x64_windows_hotspot_17.0.4.1_1.zip -O target/jre.zip -q --show-progress
echo "Setting up..."
cd target
unzip jre.zip -d .
mv $(ls -d */ | grep jre) jre
cd ..
echo "Done"
