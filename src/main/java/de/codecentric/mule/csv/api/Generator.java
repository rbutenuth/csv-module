package de.codecentric.mule.csv.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;

/**
 * Implementation of operation "generate"
 */
public class Generator {

	@Throws(value = OperationErrorTypeProvider.class)
	@MediaType(value = "application/csv")
	public InputStream generate(@TypeResolver(InputObjectMetadataResolver.class) Iterator<Map<String, String>> rows, @Config CsvConfiguration config) {
		return new ByteArrayInputStream(new byte[0]);
	}
	
}
