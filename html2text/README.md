# HtmlToTextPlugin

A BMC AR System (Helix Innovation Suite 25.1) Filter API Plugin that converts an HTML string into clean, readable plain text.  
Ideal for stripping formatting from HTML emails, user input, or rich text fields and storing the result in a plain text field.

---

## ✅ Features

- Accepts raw HTML input
- Strips all tags and removes `<script>`, `<style>`, etc.
- Cleans up excessive whitespace and line breaks
- Returns human-readable plain text
- Integrates with AR Filter Plugin Calls

---

## 📥 Input

This plugin takes **one parameter**:

1. **HTML Content** – A valid HTML string (typically from a text or rich text field)

---

## 📤 Output

- One return value: a `Value` containing a cleaned-up plain text `String`
- Can be saved directly into a `Character` (Text) field

---

## 🛠️ Compilation Instructions

### Dependencies:

You need the following JARs:

#### BMC Plugin API:
- `arapi251_build001.jar`
- `arutil251_build001.jar`
- `arpluginsvr251_build001.jar`

#### External Library:
- `jsoup-1.20.1.jar`  
  (Download from [https://jsoup.org/download](https://jsoup.org/download))

Place all JARs in a `lib/` folder (or equivalent).

### Compile:

```bash
javac --release 17 \
  -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar:./lib/jsoup-1.20.1.jar" \
  -d classes \
  HtmlToTextPlugin.java
```

### Package:
jar cf htmltotextplugin.jar -C classes .

### ⚙️ Plugin Configuration (pluginsvr_config.xml)
<plugin>
  <name>HTMLToTextPlugin</name>
  <classname>com.example.HtmlToTextPlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/htmltotextplugin.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/jsoup-1.20.1.jar</pathelement>
</plugin>


### 🧪 Example Usage in AR System Filter
Plugin Call Settings:

Plugin Name: TextPlugin
Function Name: (leave empty for ARFilterAPIPlugin)

Input Mapping:
    Param 1 → $Html_Content$ (Text or Rich Text field containing HTML)

Return Mapping:
    Result Index 0 → Text field (e.g., Plain_Text_Output)

