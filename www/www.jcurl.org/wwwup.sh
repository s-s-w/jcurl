#!/bin/sh
export LC_CTYPE="POSIX"
base=$HOME
svn=$base/bin/svn

dst=$base/www.jcurl.org
uri=svn://svn.berlios.de/jcurl/trunk/www/www.jcurl.org

cd $base
if [ -e $dst ]
then
        if [ `$svn update $dst | wc --lines` -lt 2 ]
        then
                echo "Nothing new there!"
                exit 0
        fi
else
        $svn checkout $uri $dst
fi

#$svn status $dst

# Unpack the javadocs:
tmp=$base/api.tmp
mkdir $tmp
unzip $dst/jar/jcurl-0.3.0-javadoc.jar -d $tmp > /dev/null
mv $dst/api $base/api.old
mv $tmp $dst/api
rm -rf $base/api.old
