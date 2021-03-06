#!/bin/sh
# $Id: wwwup.sh 231 2006-02-19 13:12:31Z mrohrmoser $

# the following is done at my private workstation using git:
#
## Push the svn repo dump to jcurl.org as a sort of backup
#repo=jcurl-repos.gz
#echo "backup http://svn.berlios.de/svndumps/$repo to ftp://jcurl.org"
#wget --no-verbose -O $repo http://svn.berlios.de/svndumps/$repo
#echo -e "cd public_html\nput $repo" | ftp -i -V jcurl.org
#rm $repo
#
#echo "done"
#echo "==============="

echo "update the local working copy (the http://jcurl.berlios.de/ content)"
dir=$WWWHOME
if [ `svn update $dir | wc --lines` -lt 2 ]
then
	echo "Nothing new there!"
	exit 0
fi    
