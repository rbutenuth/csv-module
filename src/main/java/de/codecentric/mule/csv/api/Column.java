package de.codecentric.mule.csv.api;

import org.mule.runtime.extension.api.annotation.param.Parameter;

public class Column {
	
	@Parameter
	private String columnName;

	@Parameter
	private ColumnType type;
	
	@Parameter
	private boolean emptyToNull;
	
	public Object parse(String value) {
		return type.parse(value, emptyToNull);
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public ColumnType getType() {
		return type;
	}

	public void setType(ColumnType type) {
		this.type = type;
	}
}
