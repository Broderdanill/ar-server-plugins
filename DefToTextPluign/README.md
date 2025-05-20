# DefToTextPlugin

A BMC AR System (Helix Innovation Suite 25.1) Filter API Plugin that reads a `.def` file (attached as an `AttachmentValue`) and returns its content as a plain text string.  
Useful for workflows that need to inspect, parse, or log contents of AR Definition export files.

---

## ✅ Features

- Accepts an attachment as input (e.g., `.def` file)
- Reads and returns the text content from the attachment
- Works with AR Filter Plugin Calls
- Supports UTF-8 encoded text files

---

## 📥 Input

This plugin takes **one parameter**:

1. **Attachment Input** – An `AttachmentValue` (e.g., from a file field)

---

## 📤 Output

- One return value: a `Value` containing the file contents as `String`
- Can be stored in a long character field or used in logging/workflow decisions

---

## 🛠️ Compilation Instructions

### Dependencies:

You need the following JARs:

#### BMC Plugin API:
- `arapi251_build001.jar`
- `arutil251_build001.jar`
- `arpluginsvr251_build001.jar`

Place all JARs in a `../arapi/` folder (or equivalent).

---

### Compile:

```bash
javac --release 17 \
  -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar" \
  -d classes \
  DefToTextPlugin.java
```

---

### Package:

```bash
jar cf deftotextplugin.jar -C classes .
```

---

## 🔧 Plugin Configuration (pluginsvr_config.xml)

Make sure the JAR is copied to the plugin server directory (e.g., `/opt/bmc/ARSystem/pluginsvr/`):

```xml
<plugin>
  <name>DefToTextPlugin</name>
  <classname>com.example.DefToTextPlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/deftotextplugin.jar</pathelement>
</plugin>
```

---

## 🧪 Example Usage in AR System Filter

Plugin Call Settings:

- **Plugin Name:** `DefToTextPlugin`
- **Function Name:** (leave empty for `ARFilterAPIPlugin`)

Input Mapping:

| Index | Typ       | Beskrivning                    |
| ----- | --------- | ------------------------------ |
| 0     | `String`  | Formulärnamn                   |
| 1     | `String`  | Request ID                     |
| 2     | `Integer` | Field ID för attachment-fältet |

Return Mapping:

- Result Index 0 → `$FileContentText$` (Character field)

