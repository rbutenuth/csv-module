package de.codecentric.mule.csv.api;

import java.math.BigDecimal;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.exception.ModuleException;

public enum ColumnType {
	TEXT {
		@Override
		protected MetadataType createMetadata(BaseTypeBuilder typeBuilder) {
			return typeBuilder.stringType().build();
		}

		@Override
		public Object parse(String text, boolean emptyToNull) {
			if (emptyToNull) {
				return text.isEmpty() ? null : text;
			} else {
				return text;
			}
		}
	},
	INTEGER {
		@Override
		protected MetadataType createMetadata(BaseTypeBuilder typeBuilder) {
			return typeBuilder.numberType().build();
		}

		@Override
		public Object parse(String text, boolean emptyToNull) {
			try {
				if (emptyToNull) {
					return text.isEmpty() ? null : Long.valueOf(text);
				} else {
					return text.isEmpty() ? Long.valueOf(0) : Long.valueOf(text);
				}
			} catch (NumberFormatException e) {
				throw new ModuleException("Unparsable long: \"" + text + "\"", CsvError.NUMBER_FORMAT, e);
			}
		}
	},
	NUMBER {
		@Override
		protected MetadataType createMetadata(BaseTypeBuilder typeBuilder) {
			return typeBuilder.numberType().build();
		}

		@Override
		public Object parse(String text, boolean emptyToNull) {
			try {
				if (emptyToNull) {
					return text.isEmpty() ? null : new BigDecimal(text);
				} else {
					return text.isEmpty() ? BigDecimal.ZERO : new BigDecimal(text);
				}
			} catch (NumberFormatException e) {
				throw new ModuleException("Unparsable BigInteger: \"" + text + "\"", CsvError.NUMBER_FORMAT, e);
			}
		}
	};

	protected abstract MetadataType createMetadata(BaseTypeBuilder typeBuilder);

	public abstract Object parse(String text, boolean emptyToNull);
	
	public String generate(Object value) {
		if (value == null) {
			return "";
		} else {
			return value.toString();
		}
	}
}
