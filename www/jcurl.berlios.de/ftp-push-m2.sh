#!/bin/sh
#
# typical usage: 
# $ time sh ftp-push-m2.sh | grep -v "File exists"
#
# upload the directory structure found in $src to $dst_host/$dst_dir
#
# uses a timestamp file $timer to upload only new stuff
#
cwd=`pwd`
#src=$HOME/.m2/repository/org/jcurl
src=/home/groups/jcurl/htdocs/m2
timer=$0.timer

dst_host=jcurl.org
#dst_dir=sandbox
dst_dir=.

cd $src

cmd="pwd\n"

# create the directory structure
cmd="$cmd"`find . -mindepth 1 -type d -printf "mkdir %p\\\\\n"`

# upload all files newer than $timer except .listing
cmd="$cmd"`find . -mindepth 1 -type f -anewer $timer -not -name ".listing" -printf "put %p\\\\\n"`

cmd="mkdir $dst_dir\ncd $dst_dir\n$cmd"
cmd=$cmd"quit"
#echo -e $cmd
echo -e $cmd | ftp -v -i $dst_host
touch $timer
cd $cwd
