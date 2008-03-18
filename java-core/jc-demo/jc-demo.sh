#!/bin/sh
cp=$HOME/.m2/repository/com/thoughtworks/xstream/xstream/1.2.1/xstream-1.2.1.jar
cp=$cp:$HOME/.m2/repository/commons-logging/commons-logging/1.0.3/commons-logging-1.0.3.jar
cp=$cp:$HOME/.m2/repository/edu/umd/cs/hcil/piccolo/1.2/piccolo-1.2.jar
cp=$cp:$HOME/.m2/repository/org/jcurl/core/jc-core/0.7-SNAPSHOT/jc-core-0.7-SNAPSHOT.jar
cp=$cp:$HOME/.m2/repository/org/jcurl/core/jc-xsio/0.7-SNAPSHOT/jc-xsio-0.7-SNAPSHOT.jar
cp=$cp:$HOME/.m2/repository/org/jcurl/core/jc-zui/0.7-SNAPSHOT/jc-zui-0.7-SNAPSHOT.jar
cp=$cp:$HOME/.m2/repository/org/jcurl/core/jc-demo/0.7-SNAPSHOT/jc-demo-0.7-SNAPSHOT.jar
cp=$cp:$HOME/.m2/repository/xpp3/xpp3_min/1.1.3.4.O/xpp3_min-1.1.3.4.O.jar
#java -Xprof -cp $cp org.jcurl.demo.zui.BroomPromptDemo
java -Xfuture -cp $cp org.jcurl.demo.tactics.MainApp
