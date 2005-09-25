#!/bin/sh
dir=/home/groups/jcurl/htdocs
svn update $dir
svn status $dir
rm -rf $dir/api
mkdir $dir/api
unzip $dir/jar/jcurl-doc-0.1.war -d $dir/api
