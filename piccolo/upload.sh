#!/bin/sh
# kopiert Piccolo Jars in das jcurl Maven2 Repository

mvnRepo=http://www.jcurl.org/m2/repo
mvnScp=scp://jcurl.org/kunden/homepages/6/d143911421/htdocs/org.jcurl.www/m2/repo
packaging=jar

function mvnup {
	mvn deploy:deploy-file -DgroupId=$groupId \
        -DartifactId=$artifactId \
        -Dversion=$version \
        -Dpackaging=$packaging \
        -Dfile=$srcFile \
        -DrepositoryId=jcurl-repository \
        -Durl=$mvnScp
}

function mvndeploy {
	mvn deploy:deploy-file -DpomFile=$pomFile \
        -Dfile=$srcFile \
        -DrepositoryId=jcurl-repository \
        -Durl=$mvnScp
}

srcFile=/home/m/tmp/piccolo-1.2/build/piccolo.jar
pomFile=piccolo.pom.xml
mvndeploy

srcFile=/home/m/tmp/piccolo-1.2/build/piccolox.jar
pomFile=piccolox.pom.xml
mvndeploy
