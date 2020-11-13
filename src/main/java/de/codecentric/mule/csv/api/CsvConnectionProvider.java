package de.codecentric.mule.csv.api;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;

public class CsvConnectionProvider implements ConnectionProvider<CsvConnection> {

	@Override
	public CsvConnection connect() throws ConnectionException {
		return new CsvConnection();
	}

	@Override
	public void disconnect(CsvConnection connection) {
		// nothing to do
	}

	@Override
	public ConnectionValidationResult validate(CsvConnection connection) {
		return ConnectionValidationResult.success();
	}

}
