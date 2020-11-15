package de.codecentric.mule.csv.api;

import java.util.Optional;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;

public class ObjectMetadataResolver {

	protected MetadataType getType(MetadataContext context, Object unusedKey)	throws MetadataResolvingException, ConnectionException {
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
