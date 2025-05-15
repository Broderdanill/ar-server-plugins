# Random Plugin
Randomize a number from-to


# Download correct versions of files to project
arapi<version>.jar
arutil<version>.jar
arpluginsvr<version>.jar

These can be copied from container running ar-server (copy files to bmcapi-folder inside randomplugin folder):
    podman cp ars-arserver-arserver:/opt/bmc/ARSystem/api/lib/arapi251_build001.jar .
    podman cp ars-arserver-arserver:/opt/bmc/ARSystem/api/lib/arutil251_build001.jar .
    podman cp ars-arserver-arserver:/opt/bmc/ARSystem/pluginsvr/arpluginsvr251_build001.jar .

2. Kompilera java-filen
    javac --release 17 -cp "../arapiarapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar" -d classes RandomNumberPlugin.java

3. Skapa jar
    jar cf randomplugin-dynamic.jar -C classes .

4. Kopiera "randomplugin-dynamic.jar" till /opt/bmc/ARSystem/pluginsvr/
    podman cp randomplugin-dynamic.jar ars-arserver-arserver:/opt/bmc/ARSystem/pluginsvr/

5. Gör jarfilen körbar
    chmod +x på randomplugin-dynamic.jar

6. Konfigurera den nya jar-filen i ar plugin server

<plugin>
  <name>RandomNumberPlugin</name>
  <classname>com.example.RandomNumberPlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/randomplugin-dynamic.jar</pathelement>
</plugin>

7. Starta om AR Server-pod



# Bonus

Verifiera jar-filen:
    jar tf randomplugin-dynamic.jar