#!/bin/sh

#mvn -Dmaven.test.failure.ignore=true clean install

main=$1
case "$main" in 
	v ) 
		main=org.jcurl.demo.viewer.ViewerApp
	;;
	t ) 
		main=org.jcurl.demo.tactics.TacticsApp
	;;
	e ) 
		main=org.jcurl.demo.editor.EditorApp
	;;
	viewer ) 
		main=org.jcurl.demo.viewer.ViewerApp
	;;
	tactics ) 
		main=org.jcurl.demo.tactics.TacticsApp
	;;
	editor ) 
		main=org.jcurl.demo.editor.EditorApp
	;;
esac

base=$HOME/.m2/repository
cp=
cp=$base/org/jcurl/jcurl-demo/0.7.0/jcurl-demo-0.7.0.jar
cp=$cp:$base/org/jcurl/jcurl-core/0.7.0/jcurl-core-0.7.0.jar
cp=$cp:$base/commons-logging/commons-logging/1.1/commons-logging-1.1.jar
cp=$cp:$base/net/java/dev/swing-layout/swing-layout/1.0.1/swing-layout-1.0.1.jar

ls -l ${cp//:/ }

java -cp $cp $main
