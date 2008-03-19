#!/bin/sh
mvn deploy:deploy-file -DpomFile=scenario.pom.xml -Dfile=Scenario-0.6.jar -DrepositoryId=ssh-jcurl-repo -Durl=scp://jcurl.org/kunden/homepages/6/d143911421/htdocs/org.jcurl.www/m2/repo
