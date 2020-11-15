# Mule Csv-Module Extension

The Mule Csv-Module is an adapter to use https://commons.apache.org/proper/commons-csv/ within you Mule application. 
You can use it to parse or generate csv files. It allows some more options than the csv support available in DataWeave.

You have to define your csv format in a global configuration, but as a benefit you get full metadata support and
the possibility to work with csv files without header line. 

The configuration fields support online help, which I will not repeat here.  

Add this dependency to your application pom.xml (check for newer version):

```
<dependency>
	<groupId>de.codecentric.mule</groupId>
	<artifactId>csv-module</artifactId>
	<version>0.1.0</version>
	<classifier>mule-plugin</classifier>
</dependency>
```
