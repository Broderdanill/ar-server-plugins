# Base64ToAttachmentPlugin

This Java plugin for BMC AR Server 25.1 takes a Base64 string and filename as input,
and returns a valid AR AttachmentValue.

## Requirements
- Java JDK 17 (or whatever your AR Server plugin JVM supports)
- arapi251_build001.jar
- arutil251_build001.jar
- arpluginsvr251_build001.jar

## Build Instructions (Linux/Mac)

1. Create output dir:
   mkdir classes

2. Compile:
   javac -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar" -d classes Base64ToAttachmentPlugin.java

3. Create JAR:
   jar cf base64toattachment.jar -C classes .

4. Plugin config (pluginsvr_config.xml):
   <plugin>
     <name>AttachmentPlugin</name>
     <classname>com.example.Base64ToAttachmentPlugin</classname>
     <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/base64toattachment.jar</pathelement>
   </plugin>

5. Restart plugin server:
   systemctl restart arplugin

## Filter Setup in Developer Studio
- Action: Call Plugin
- Plugin Name: AttachmentPlugin
- Function Name: Base64ToAttachment
- Input Mapping: 
    - Base64 string
    - Filename (e.g. "file.txt")
- Return Mapping: Attachment field