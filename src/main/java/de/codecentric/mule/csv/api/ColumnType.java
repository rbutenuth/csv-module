package de.codecentric.mule.csv.api;

import java.math.BigDecimal;

public enum ColumnType {
	TEXT {
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
		public Object parse(String text, boolean emptyToNull) {
			if (emptyToNull) {
				return text.isEmpty() ? null : new BigDecimal(text);
			} else {
				return text.isEmpty() ? BigDecimal.ZERO : new BigDecimal(text);
			}
		}
	};

	public abstract Object parse(String text, boolean emptyToNull);
}
