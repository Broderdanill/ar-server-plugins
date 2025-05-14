# Requirements
jsoup-1.20.1.jar - https://jsoup.org/download


# Compile
javac --release 17 -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar:./lib/jsoup-1.20.1.jar" -d classes  HtmlToTextPlugin.java

### Package
jar cf htmltotextplugin.jar -C classes .