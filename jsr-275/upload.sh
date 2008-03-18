#!/bin/sh
mvn deploy:deploy-file -DpomFile=jsr-275.pom -Dfile=jsr-275.jar -DrepositoryId=ssh-jcurl-repo -Durl=scp://jcurl.org/kunden/homepages/6/d143911421/htdocs/org.jcurl.www/m2/repo
