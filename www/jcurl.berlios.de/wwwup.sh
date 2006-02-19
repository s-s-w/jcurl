#!/bin/sh
# $Id: wwwup.sh 231 2006-02-19 13:12:31Z mrohrmoser $
dir=/home/groups/jcurl/htdocs

if [ `svn update $dir | wc --lines` -lt 2 ]
then
	echo "Nothing new there!"
    exit 0
fi    
