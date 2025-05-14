# HtmlToPdfPlugin

A BMC AR System (Helix Innovation Suite 25.1) Filter API Plugin that converts an HTML string into a PDF file and returns it as an `AttachmentValue`.  
Useful for generating PDF documents (e.g., reports, invoices) directly from form data.

## ✅ Features

- Accepts raw HTML input
- Generates valid PDF output
- Returns the PDF as an attachment to be stored in AR System
- Works with AR Filter Plugin Calls

---

## 📥 Input

This plugin takes **one parameter**:

1. **HTML Content** – A valid HTML string (typically from a text field)

> ⚠️ PDF filename is currently fixed as `generated.pdf`. Can be enhanced to accept filename as second parameter.

---

## 📤 Output

- One return value: a `Value` containing an `AttachmentValue` with the generated PDF.
- Can be saved to any `Attachment` field on the same form.

---

## 🛠️ Compilation Instructions

### Dependencies:

You need the following JARs:

#### BMC Plugin API:
- `arapi251_build001.jar`
- `arutil251_build001.jar`
- `arpluginsvr251_build001.jar`

#### External Libraries:
- `openhtmltopdf-core-1.0.10.jar`
- `openhtmltopdf-pdfbox-1.0.10.jar`
- `pdfbox-3.0.5.jar`
- `fontbox-3.0.5.jar`
- `commons-io-20030203.000550.jar`

Place all JARs in a `lib/` folder (or equivalent).

### Compile:

```bash
javac --release 17 \
  -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar:./lib/commons-io-20030203.000550.jar:./lib/fontbox-3.0.5.jar:./lib/openhtmltopdf-core-1.0.10.jar:./lib/openhtmltopdf-pdfbox-1.0.10.jar:./lib/pdfbox-3.0.5.jar" \
  -d classes \
  HtmlToPdfPlugin.java
  ```
  
### Package
jar cf htmltopdfplugin.jar -C classes .


### Plugin Configuration (pluginsvr_config.xml) - Copy all needed files to pluginsvrfolder aswell

<plugin>
  <name>PdfPlugin</name>
  <classname>com.example.HtmlToPdfPlugin</classname>
  <filename>/opt/bmc/ARSystem/pluginsvr/htmltopdfplugin.jar</filename>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/htmltopdfplugin.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/openhtmltopdf-pdfbox-1.0.10.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/openhtmltopdf-core-1.0.10.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/pdfbox-3.0.5.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/fontbox-3.0.5.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/commons-io-20030203.000550.jar</pathelement>
</plugin>


### Example Usage in AR System Filter
Plugin Call Settings:
  Plugin Name: PdfPlugin
  Function Name: (leave empty for ARFilterAPIPlugin)
  
  Input Mapping:
    Param 1 → $Html_Content$ (Text field containing HTML)

  Return Mapping:
    Result Index 0 → Attachment field (e.g., PDF_File)