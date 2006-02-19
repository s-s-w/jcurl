#!/bin/sh
export LC_CTYPE="POSIX"
base=/kunden/homepages/6/d143911421/htdocs
#base=$HOME
echo "$HOME" > $base/recent.log
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

tmp=$base/api.tmp

$svn status $dir
mkdir $tmp
unzip $dst/jar/jcurl-doc-0.3.war -d $tmp > /dev/null
mv $dst/api $base/api.old
mv $tmp $dst/api
rm -rf $base/api.old
