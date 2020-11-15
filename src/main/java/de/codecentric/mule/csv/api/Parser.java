package de.codecentric.mule.csv.api;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.FlowListener;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

/**
 * Implementation of operation "parse".
 */
public class Parser {

	@OutputResolver(output = OutputObjectMetadataResolver.class)
	@Throws(value = OperationErrorTypeProvider.class)
	public PagingProvider<CsvConnection, Map<String, Object>> parse(InputStream input, FlowListener flowListener,
			@Config CsvConfiguration config) throws IOException {
		flowListener.onComplete(new Runnable() {
			@Override
			public void run() {
				close(input);
			}
		});

		Reader rd = new InputStreamReader(input, config.getCharset());
		CSVFormat format = config.buildCsvFormat();
		CSVParser parser = format.parse(rd);
		Iterator<CSVRecord> records = parser.iterator();
		List<Column> columns = config.getColumns();
		if (config.isWithHeaderLine()) {
			checkHeader(records, columns);
		}
		return new PagingProvider<CsvConnection, Map<String, Object>>() {
			final int PAGE_SIZE = 100;

			@Override
			public List<Map<String, Object>> getPage(CsvConnection connection) {
				List<Map<String, Object>> result = new ArrayList<>();
				for (int i = 0; i < PAGE_SIZE; i++) {
					if (records.hasNext()) {
						Map<String, Object> map = new LinkedHashMap<>();
						CSVRecord record = records.next();
						for (Column column : columns) {
							map.put(column.getColumnName(), column.parse(record.get(column.getColumnName())));
						}
						result.add(map);
					} else {
						Parser.this.close(parser);
						break;
					}
				}
				return result;
			}

			@Override
			public Optional<Integer> getTotalResults(CsvConnection connection) {
				return java.util.Optional.empty();
			}

			@Override
			public void close(CsvConnection connection) {
				Parser.this.close(parser);
			}
		};
	}

	private void close(Closeable resource) {
		try {
			resource.close();
		} catch (IOException e) {
			// ignore exceptions on close
		}
	}

	private void checkHeader(Iterator<CSVRecord> records, List<Column> columns) {
		if (!records.hasNext()) {
			throw new ModuleException("Missing header line", CsvError.HEADER_MISSING);
		}
		CSVRecord header = records.next();
		if (header.size() != columns.size()) {
			throw new ModuleException(
					"Header should contain " + columns.size() + " columns, but contains " + header.size(),
					CsvError.HEADER_MISMATCH);
		}
		for (int i = 0; i < header.size(); i++) {
			if (!header.get(i).equals(columns.get(i).getColumnName())) {
				throw new ModuleException(
						"Column '" + columns.get(i).getColumnName() + "' excepted, but got '" + header.get(i) + "'",
						CsvError.HEADER_MISMATCH);
			}
		}
	}
}
