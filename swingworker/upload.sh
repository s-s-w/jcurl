#!/bin/sh
mvn deploy:deploy-file -DpomFile=swingworker.pom.xml -Dfile=swing-worker-1.1.jar -DrepositoryId=ssh-jcurl-repo -Durl=scp://shell.berlios.de/home/groups/jcurl/htdocs/m2/repo
