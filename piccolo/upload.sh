#!/bin/sh
# upload piccolo jars to the jcurl maven2 repository

mvnScp=scp://shell.berlios.de/home/groups/jcurl/htdocs/m2/repo
piccoloBase=$HOME/work/piccolo2d/piccolo-1.2.1
packaging=jar

function mvnup {
	mvn deploy:deploy-file -DgroupId=$groupId \
        -DartifactId=$artifactId \
        -Dversion=$version \
        -Dpackaging=$packaging \
        -Dfile=$srcFile \
        -DrepositoryId=ssh-jcurl-repo \
        -Durl=$mvnScp
}

function mvndeploy {
	mvn deploy:deploy-file -DpomFile=$pomFile \
        -Dfile=$srcFile \
        -DrepositoryId=ssh-jcurl-repo \
        -Durl=$mvnScp
}

srcFile=$piccoloBase/build/piccolo.jar
pomFile=piccolo.pom.xml
mvndeploy

srcFile=$piccoloBase/build/piccolox.jar
pomFile=piccolox.pom.xml
mvndeploy

srcFile=$piccoloBase/build/examples.jar
pomFile=examples.pom.xml
mvndeploy
