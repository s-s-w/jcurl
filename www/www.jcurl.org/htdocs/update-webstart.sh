#!/bin/sh
# Copy given webstart apps from the maven repo to the webspace.

groupId=org.jcurl.demo
version=0.7.3
modules="viewer tactics"

srczipbase=$HOME/.m2/repository
srczipbase=$HOME/org.jcurl.www/m2/repo
dstwebbase=$HOME/org.jcurl.www/jws

for m in $modules ; do
	src=$srczipbase/${groupId//.//}/$m/$version/$m-$version.zip
	dst=$dstwebbase/${groupId//.//}/$m/$version
#	ls -Al $src $dst
	mkdir -p $dst
	rm -rf $dst
	mkdir -p $dst
	unzip $src -d $dst
done