# HtmlToPdfPlugin

This plugin converts an HTML string into a PDF file and returns it as an AttachmentValue.

## Function Name: HtmlToPdf

### Input Parameters:
1. HTML string (valid HTML markup)
2. Output filename (e.g., report.pdf)

### Output:
- PDF file as AttachmentValue

## Compile Instructions

Requires:
- arapi251_build001.jar
- arutil251_build001.jar
- arpluginsvr251_build001.jar
- openhtmltopdf-pdfbox-1.0.10.jar (and dependencies)

Example compile:
javac -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar" -d classes HtmlToPdfPlugin.java

Create JAR:
jar cf htmltopdfplugin.jar -C classes .

## Plugin Config Example

<plugin>
  <name>PdfPlugin</name>
  <classname>com.example.HtmlToPdfPlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/htmltopdfplugin.jar</pathelement>
</plugin>

## Usage in Filter:
- Plugin Name: PdfPlugin
- Function Name: HtmlToPdf
- Input Mapping:
    - $Html_Content$ (HTML string field)
    - "document.pdf"
- Return Mapping:
    - Result index 0 → Attachment field