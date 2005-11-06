#!/bin/sh
dir=/home/groups/jcurl/htdocs
tmp=$HOME/api.tmp
svn update $dir
svn status $dir
mkdir $tmp
unzip $dir/jar/jcurl-doc-0.2.war -d $tmp > /dev/null
mv $dir/api $HOME/api.old
mv $tmp $dir/api
rm -rf $HOME/api.old
