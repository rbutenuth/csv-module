# Mule Csv-Module Extension

## Introduction

The Mule Csv-Module is an adapter to use https://commons.apache.org/proper/commons-csv/ within you Mule application. 
You can use it to parse or generate csv files. It allows some more options than the csv support available in DataWeave.

You have to define your csv format in a global configuration, but as a benefit you get full metadata support and
the possibility to work with csv files without header line. 

The configuration fields support online help, which I will not repeat here.  

## Maven Dependency

Add this dependency to your application pom.xml (check for newer version):

```
<dependency>
	<groupId>de.codecentric.mule.modules</groupId>
	<artifactId>csv-module</artifactId>
	<version>0.1.0</version>
	<classifier>mule-plugin</classifier>
</dependency>
```

The module is available on [Maven Central](https://mvnrepository.com/), so you don't need it to compile/install it yourself.

## Example

Let's assume you want to handle a csv file like this:

```
name|age|weight
Max Mule|12|79.8
Molly Mule|10|65.2
```

Three columns with types:
* name, type text
* age, type integral number
* weight, number with decimals

The column separator is `|`, the record separator is a new line.

### Global Configuration

```
<csv:config name="Csv_Config" doc:name="Csv Config" delimiter="|" recordSeparator="\n">
	<csv:columns >
		<csv:column columnName="name" type="TEXT" />
		<csv:column columnName="age" type="INTEGER" />
		<csv:column columnName="weight" type="NUMBER" />
	</csv:columns>
</csv:config>
```

### Parse CSV

As input for the parser you need an `InputStream`, so you can place it behind a POST HTTP:LISTENER, a file read, etc.:

```
<csv:parse doc:name="Parse" config-ref="Csv_Config" input="#[payload]"/>
```

When you place a DataWeave behind the `csv:parse`, you see the metadata of the csv input.

### Generate CSV

The generator needs a list of maps, so a DataWeave with `output application/java` is fine. In a DataWeave in front
of a `csv:generate` you see the metadata on the right side of the transformation.

```
<csv:generate doc:name="Generate" config-ref="Csv_Config" rows="#[payload]"/>
```

