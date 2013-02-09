#!/bin/sh
mkdir tmp
#Retrofit
cd tmp
git clone https://github.com/square/retrofit.git
cd retrofit
mvn clean install
cd ..
rm -rf retrofit
#android-menudrawer
git clone https://github.com/SimonVT/android-menudrawer.git
cd android-menudrawer
git checkout -b dev origin/dev
mvn clean install
cd ..
rm -rf retrofit
#dagger
git clone https://github.com/square/dagger.git
cd dagger
mvn clean install
cd ..
rm -rf dagger
cd ..
rm -rf tmp

