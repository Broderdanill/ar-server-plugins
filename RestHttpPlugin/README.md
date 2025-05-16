# RestHttpPlugin
Dynamic REST-plugin for AR Server (BMC Helix Innovation Suite 25.1)


## Return format


### On success:


{
  "success": true,
  "status": 200,
  "body": "..."
}


### On error:


{
  "success": false,
  "error": "HTTP-anrop misslyckades: ..."
}


## Input parameters (7):


1. HTTP-metod (GET, POST, ...)
2. URL
3. Headers som JSON-sträng
4. Body/data
5. Auth-typ: `none`, `basic`, `bearer`, `header`, `cert`
6. Auth-värde 1
7. Auth-värde 2


## Compile


requires `json-20250107.jar`


```
javac --release 17 \
  -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar:./lib/json-20250107.jar" \
  -d classes \
  RestHttpPlugin.java

```

## Package

jar cf resthttpplugin.jar -C classes .

## Plugin XML config:

<plugin>
  <name>RestHttpPlugin</name>
  <classname>com.example.RestHttpPlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/resthttpplugin.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/json-20250107.jar</pathelement>
</plugin>
