package de.codecentric.mule.csv.api;

import static org.mule.sdk.api.meta.JavaVersion.JAVA_11;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_17;
import static org.mule.sdk.api.meta.JavaVersion.JAVA_8;

import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;
import org.mule.sdk.api.annotation.JavaVersionSupport;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "csv")
@Extension(name = "Csv")
@Configurations(CsvConfiguration.class)
@Operations({Generator.class, Parser.class})
@ConnectionProviders({CsvConnectionProvider.class})
@ErrorTypes(value = CsvError.class)
@JavaVersionSupport({ JAVA_8, JAVA_11, JAVA_17})
public class CsvExtension {
	// This class is just holder for the annotations.
}
