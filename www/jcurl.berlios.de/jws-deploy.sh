#!/bin/sh
#
# Deploy java webstart bundles to a webserver.
#
mvnrepo=/home/groups/jcurl/htdocs/m2/snap
jwsroot=/home/groups/jcurl/htdocs/jws

if [ "$1" == "" ]
then
	echo "Deploy maven (webstart) artifact zip files to a webserver directory"
	echo ""
	echo "Maven Repo   : $mvnrepo"
	echo "Webstart Root: $jwsroot"
	echo ""
	echo "Usage:"
	echo -e "\t$0 item+"
	echo ""
	echo "Example:"
	echo -e "\t$0 org.jcurl.demo:tactics:0.7-SNAPSHOT:zip org.jcurl.demo:viewer:0.7-SNAPSHOT:zip"
	echo ""
	exit 1
fi

while [ "$1" != "" ]
do
	# first split the item name
	if [ `expr match "$1" '^[^:]*:[^:]*:[^:]*:[^:]*$'` -le 0 ]
	then
		echo "ERROR: Couldn't parse '$1' - must match 'groupId:artifactId:version:packaging'" >&2
	fi
	groupId=`expr match "$1" '^\([^:]*\):[^:]*:[^:]*:[^:]*$'`
	artifactId=`expr match "$1" '^[^:]*:\([^:]*\):[^:]*:[^:]*$'`
	version=`expr match "$1" '^[^:]*:[^:]*:\([^:]*\):[^:]*$'`
	packaging=`expr match "$1" '^[^:]*:[^:]*:[^:]*:\([^:]*\)$'`
	# next find the most recent SNAPSHOT - if it's a snapshot version
	if [ `expr match "$version" '.*-SNAPSHOT$'` -gt 0 ]
	then
		src_path=`ls $mvnrepo/${groupId//\./\/}/$artifactId/$version/$artifactId-*.$packaging | sort | tail -n 1`
	else
		src_path=$mvnrepo/${groupId//\./\/}/$artifactId/$version/$artifactId-$version.$packaging
	fi
	dst_path=$jwsroot/${groupId//\./\/}/$artifactId/$version
	unset groupId
	unset artifactId
	unset version
	unset packaging

	if [ $dst_path -ot $src_path ]
	then
		rm -r $dst_path 2> /dev/null
		mkdir -p $dst_path
		unzip -d $dst_path $src_path
	#else
	#	echo "Update NOT necessary" >&2
	fi
	unset src_path
	unset dst_path
	shift
done

