package de.codecentric.mule.csv.api;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

public enum CsvError implements ErrorTypeDefinition<CsvError> {
	HEADER_MISSING,
	HEADER_MISMATCH,
	IO_ERROR
}
