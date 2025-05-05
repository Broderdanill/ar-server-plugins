# URLEncoderPlugin

This AR Server Java plugin URL-encodes a given string using UTF-8 encoding.

## Function Name: URLEncode

### Input Parameters:
1. Input string (e.g., "foo bar?x=1&y=2")

### Return:
- URL-encoded string (e.g., "foo%20bar%3Fx%3D1%26y%3D2")

## Compilation Instructions:

javac --release 17 -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar" -d classes URLEncoderPlugin.java

jar cf urlencoderplugin.jar -C classes .

## Plugin XML config:

<plugin>
  <name>URLPlugin</name>
  <classname>com.example.URLEncoderPlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/urlencoderplugin.jar</pathelement>
</plugin>

## Filter setup:
- Call Plugin action
- Plugin Name: URLPlugin
- Function Name: URLEncode
- Input: [$InputString$]
- Return Mapping: → target character field