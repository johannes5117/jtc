#!/bin/sh

mkdir target/classes/META-INF
mkdir target/classes/META-INF/services
echo "com.johannes.lsctic.panels.gui.plugins.$1.$1" > target/classes/META-INF/services/com.johannes.lsctic.panels.gui.plugins.AddressPlugin
cd target/classes
jar cf $1.jar com/johannes/lsctic/panels/gui/plugins/$1/* META-INF
rm -R META-INF
cp $1.jar /home/johannes/JTC/plugin/$1.jar
rm $1.jar

