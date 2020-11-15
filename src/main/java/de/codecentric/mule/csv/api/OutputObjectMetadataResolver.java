package de.codecentric.mule.csv.api;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class OutputObjectMetadataResolver extends ObjectMetadataResolver implements OutputTypeResolver<Object> {

	@Override
	public String getCategoryName() {
		return "CsvCategory";
	}

	@Override
	public MetadataType getOutputType(MetadataContext context, Object unusedKey) throws MetadataResolvingException, ConnectionException {
		return getType(context, unusedKey);
	}
}
