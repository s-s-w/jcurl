#!/bin/sh
# $Id: wwwup.sh 231 2006-02-19 13:12:31Z mrohrmoser $

# Push the svn repo dump to jcurl.org
repo=jcurl-repos.gz
wget -O $repo http://svn.berlios.de/svndumps/$repo
echo -e "put $repo" | ftp -i jcurl.org
rm $repo

# update the local working copy (the http://jcurl.berlios.de/ content)
dir=$WWWHOME
if [ `svn update $dir | wc --lines` -lt 2 ]
then
	echo "Nothing new there!"
	exit 0
fi    
