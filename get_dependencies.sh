#!/bin/sh
mkdir tmp
#Retrofit
cd tmp
git clone https://github.com/square/retrofit.git
cd retrofit
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

