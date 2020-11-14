package de.codecentric.mule.csv.api;

import java.math.BigDecimal;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;

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
			if (emptyToNull) {
				return text.isEmpty() ? null : Long.valueOf(text);
			} else {
				return text.isEmpty() ? Long.valueOf(0) : Long.valueOf(text);
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
			if (emptyToNull) {
				return text.isEmpty() ? null : new BigDecimal(text);
			} else {
				return text.isEmpty() ? BigDecimal.ZERO : new BigDecimal(text);
			}
		}
	};

	protected abstract MetadataType createMetadata(BaseTypeBuilder typeBuilder);

	public abstract Object parse(String text, boolean emptyToNull);
}
