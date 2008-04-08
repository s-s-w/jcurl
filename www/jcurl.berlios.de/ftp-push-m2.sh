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
reporter=/home/groups/jcurl/htdocs/ftp-push-m2-to-html.awk
#src=$HOME/.m2/repository/org/jcurl
src=/home/groups/jcurl/htdocs/m2
timer=$0.timer
report=`basename $0`.html

dst_host=jcurl.org
# temporary address while KK-ing:
dst_host=213.133.104.85
#dst_dir=sandbox
dst_dir=.

cd $src

cmd="pwd\n"

# create the directory structure
cmd="$cmd"`find . -mindepth 1 -type d -printf "mkdir %p\\\\\n"`

# upload all files newer than $timer except wagon*.zip
cmd="$cmd"`find . -mindepth 1 -type f -anewer $timer -not -name "wagon*.zip" -printf "put %p\\\\\n"`

cmd="mkdir $dst_dir\ncd $dst_dir\n$cmd"
cmd=$cmd"quit"
#echo -e $cmd
echo -e $cmd | ftp -v -i $dst_host | $reporter > $report
touch $timer

# upload the report itself
echo -e "cd $dst_dir\nput $report status.html\nquit" | ftp -i $dst_host

cd $cwd
