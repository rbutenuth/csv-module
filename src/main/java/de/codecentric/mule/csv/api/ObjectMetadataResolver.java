package de.codecentric.mule.csv.api;

import java.util.Optional;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class ObjectMetadataResolver implements OutputTypeResolver<Object> {

	
	// TODO Kann gleichzeitig InptTypeResolver implementieren
	// TODO https://docs.mulesoft.com/mule-sdk/1.1/static-metadata für den statischen Teil (MimeType csv)
	
	@Override
	public String getCategoryName() {
		return "CsvCategory";
	}

	@Override
	public MetadataType getOutputType(MetadataContext context, Object unusedKey)
			throws MetadataResolvingException, ConnectionException {
		BaseTypeBuilder typeBuilder = context.getTypeBuilder();
		ObjectTypeBuilder record = typeBuilder.objectType();

		Optional<CsvConfiguration> optConfig = context.getConfig();
		if (optConfig.isPresent()) {
			CsvConfiguration config = optConfig.get();
			for (Column column : config.getColumns()) {
				ObjectFieldTypeBuilder columnBuilder = record.addField();
				columnBuilder.key(column.getColumnName());
				columnBuilder.required(true);
				columnBuilder.value(column.getType().createMetadata(typeBuilder));
			}
		}

		return record.build();
	}

}
