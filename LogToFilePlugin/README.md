# LogToFilePlugin

A **BMC AR System (Helix Innovation Suite 25.1)** Filter API Plugin for structured, timestamped logging of messages to file.

This plugin is designed to integrate seamlessly into AR workflow filters and supports log collection agents like **Fluent Bit**, **Filebeat**, or custom ingestion tools.

---

## ✅ Features

- Log messages to any custom file path
- Automatic timestamping (UTC, ISO 8601)
- Includes log level and application context
- Plain-text format optimized for parsing
- Simple configuration and deployment
- Requires no external libraries (pure Java)

---

## 📥 Input Parameters

This plugin accepts **four parameters** via the AR Filter Plugin API:

| Parameter   | Type   | Description                                                   |
|-------------|--------|---------------------------------------------------------------|
| `log_file`  | String | Full path to log file (e.g. `/opt/bmc/ARSystem/db/log.txt`)   |
| `message`   | String | The message to log                                            |
| `log_level` | String | Severity (e.g. `INFO`, `WARN`, `ERROR`, `DEBUG`)              |
| `application` | String | Name of the calling application or module                  |

---

## 📤 Output Format

Each log entry is written as a **single line** in this format:

```
2025-06-09T14:45:12Z [UserSync] [ERROR] User import failed: LDAP timeout
```

### Format Details:
- **Timestamp**: UTC in ISO 8601 format
- **Application**: Provided by caller
- **Log Level**: Provided by caller
- **Message**: Custom string, typically error or audit text

---

## 🛠️ Build Instructions

### Prerequisites:
- Java 17
- BMC AR Plugin JARs:
  - `arapi251_build001.jar`
  - `arutil251_build001.jar`
  - `arpluginsvr251_build001.jar`

### Compile:
```bash
javac --release 17 -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar" -d classes LogToFilePlugin.java
```

### Package:
```bash
jar cf logtofileplugin.jar -C classes .
```

---

## 🔧 Plugin Configuration

### `pluginsvr_config.xml`:

```xml
<plugin>
  <name>LogToFilePlugin</name>
  <classname>com.example.arfilter.LogToFilePlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/logtofileplugin.jar</pathelement>
</plugin>
```

Or in `ar.cfg` (legacy):

```
Plugin-Filter-LogToFilePlugin: logtofileplugin LogToFilePlugin
```

---

## 🧪 AR System Filter Example

### Plugin Call:
| Setting        | Value                |
|----------------|----------------------|
| Plugin Name    | `LogToFilePlugin`    |
| Function Name  | *(leave empty)*      |

### Input Mapping:
1. `/opt/bmc/ARSystem/db/myapp.log`
2. `$Log_Message$`
3. `"ERROR"`
4. `"UserSync"`

### Output Mapping:
None required – the plugin writes directly to file.

---

## 📄 License

This plugin is released under the MIT License. See `LICENSE` for details.

---

## 👨‍💻 Maintainer

Developed by [Your Name / Team]  
Contact: [your.email@example.com]