#!/bin/sh
mvn deploy:deploy-file -DpomFile=beansbinding.pom.xml -Dfile=beansbinding-1.2.1.jar -DrepositoryId=ssh-jcurl-repo -Durl=scp://shell.berlios.de/home/groups/jcurl/htdocs/m2/repo
