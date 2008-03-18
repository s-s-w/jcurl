#!/bin/sh
src=`pwd`/jscience.jar
tmp=jscience-tmp
cwd=`pwd`
mkdir $tmp
cd $tmp
unzip $src
rm -r META-INF javolution org javax/realtime
find . -type f | xargs jar -cf $cwd/jsr-275.jar 
cd $cwd
rm -rf $tmp
