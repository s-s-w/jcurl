#!/bin/sh
mvn deploy:deploy-file -DpomFile=appframework.pom.xml -Dfile=AppFramework-1.03.jar -DrepositoryId=ssh-jcurl-repo -Durl=scp://shell.berlios.de/home/groups/jcurl/htdocs/m2/repo
