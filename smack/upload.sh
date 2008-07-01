#!/bin/sh
mvn deploy:deploy-file -DpomFile=smack.pom.xml -Dfile=smack.jar -DrepositoryId=ssh-jcurl-repo -Durl=scp://shell.berlios.de/home/groups/jcurl/htdocs/m2/repo
