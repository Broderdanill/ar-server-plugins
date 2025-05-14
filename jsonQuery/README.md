# JsonQueryPlugin

A BMC AR System (Helix Innovation Suite 25.1) Filter API Plugin that extracts specific values from complex JSON structures using flexible JSONPath expressions.

Perfect for working with nested JSON blobs stored in character fields – such as API responses or structured user input.

---

## ✅ Features

- Accepts full JSON input (simple or deeply nested)
- Queries values using JSONPath expressions (e.g. `user.name`, `order.items[0].price`)
- Returns the matching value as plain text
- Supports object, array, string, number, and boolean values
- Easily integrates into AR Filter Plugin Calls

---

## 📥 Input

This plugin takes **two parameters**:

1. **JSON Content** – a valid JSON string (from a text field)
2. **JSON Path Expression** – a query string like:
   - `user.name`
   - `order.items[0].price`
   - `data.customers[1].email`

> ⚠️ JSONPath expression must be relative (without `$` – it will be prepended automatically).

---

## 📤 Output

- One return value: a `Value(String)` containing the result of the query.
  - If the result is a primitive (string/number/boolean), it's returned directly.
  - If it's an object/array, it's returned as a JSON-formatted string.

---

## 🛠️ Compilation Instructions

### Dependencies:

You need the following JARs:

#### BMC Plugin API:
- `arapi251_build001.jar`
- `arutil251_build001.jar`
- `arpluginsvr251_build001.jar`

#### External Libraries:
- `json-path-2.9.0.jar`
- `jackson-core-2.19.0.jar`
- `jackson-databind-2.19.0.jar`
- `jackson-annotations-2.19.0.jar`

Download dependencies from [https://search.maven.org/](https://search.maven.org/) or [https://github.com/json-path/JsonPath](https://github.com/json-path/JsonPath)

Place all JARs in a `lib/` folder.

### Compile:

```bash
javac --release 17 \
  -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar:./lib/json-path-2.9.0.jar:./lib/jackson-core-2.19.0.jar:./lib/jackson-databind-2.19.0.jar:./lib/jackson-annotations-2.19.0.jar" \
  -d classes \
  JsonQueryPlugin.java
```

### Package
jar cf jsonqueryplugin.jar -C classes .

### ⚙️ Plugin Configuration (pluginsvr_config.xml)
<plugin>
  <name>JsonPlugin</name>
  <classname>com.example.JsonQueryPlugin</classname>
  <filename>/opt/bmc/ARSystem/pluginsvr/jsonqueryplugin.jar</filename>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/jsonqueryplugin.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/json-path-2.9.0.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/jackson-core-2.19.0.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/jackson-databind-2.19.0.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/jackson-annotations-2.19.0.jar</pathelement>

</plugin>


### 🧪 Example Usage in AR System Filter

Plugin Call Settings:

Plugin Name: JsonPlugin
Function Name: (leave empty for ARFilterAPIPlugin)
Input Mapping:
    Param 1 → $Json_Content$ (Text field containing the full JSON)
    Param 2 → "user.name" (The JSONPath expression)

Return Mapping:
    Result Index 0 → Text field (e.g., Json_Value_Result)

