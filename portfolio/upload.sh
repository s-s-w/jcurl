#!/bin/sh
# kopiert NCSA Portfolio Jars in das jcurl Maven2 Repository

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
        -Dfile="$srcFile" \
        -DrepositoryId=jcurl-repository \
        -Durl=$mvnScp
}

srcFile="/home/m/tmp/KGPJ Code/Tour3D/ncsa/portfolio.jar"
pomFile=portfolio.pom.xml
mvndeploy
