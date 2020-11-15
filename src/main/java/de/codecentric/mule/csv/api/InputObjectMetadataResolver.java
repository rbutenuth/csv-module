package de.codecentric.mule.csv.api;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;

public class InputObjectMetadataResolver extends ObjectMetadataResolver implements InputTypeResolver<Object> {
	
	@Override
	public String getCategoryName() {
		return "CsvCategory";
	}

	@Override
	public MetadataType getInputMetadata(MetadataContext context, Object unusedkey) throws MetadataResolvingException, ConnectionException {
		return getType(context, unusedkey);
	}
}
