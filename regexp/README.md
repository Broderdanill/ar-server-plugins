# RegexMatchPlugin

This AR Server Java plugin evaluates a regular expression against a string
and returns the first full match (like Matcher.find() + group()).

## Function Name: RegexMatch

### Input Parameters:
1. Input text (String)
2. Regex pattern (String)

### Return:
- First match as a string (or empty string if no match)

## Compilation Instructions:

javac -cp "arapi251_build001.jar:arutil251_build001.jar:arpluginsvr251_build001.jar" -d classes RegexMatchPlugin.java

jar cf regexmatchplugin.jar -C classes .

## Plugin XML config:

<plugin>
  <name>RegexPlugin</name>
  <classname>com.example.RegexMatchPlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/regexmatchplugin.jar</pathelement>
</plugin>

## Filter setup:
- Call Plugin action
- Plugin Name: RegexPlugin
- Function Name: RegexMatch
- Input: [$InputText$], ["your-regex"]
- Return Mapping: → target character field

Example regex for email validation:
^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,}$