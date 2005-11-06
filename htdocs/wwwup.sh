#!/bin/sh
# $Id$
dir=/home/groups/jcurl/htdocs
#dir=/home/m/eclipse/berlios/htdocs
tmp=$HOME/api.tmp

if [ `svn status --show-updates --quiet $dir | wc --lines` -lt 2 ]
then
	echo "Nothing new there!"
    exit 0
fi    

svn update $dir
svn status $dir
mkdir $tmp
unzip $dir/jar/jcurl-doc-0.2.war -d $tmp > /dev/null
mv $dir/api $HOME/api.old
mv $tmp $dir/api
rm -rf $HOME/api.old
