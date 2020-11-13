package de.codecentric.mule.csv.api;

import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "csv")
@Extension(name = "Csv")
@Configurations(CsvConfiguration.class)
@ConnectionProviders({CsvConnectionProvider.class})
@ErrorTypes(value = CsvError.class)
public class CsvExtension {
	// This class is just holder for the annotations.
}
