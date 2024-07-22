package de.codecentric.mule.csv.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class GeneratorTest {
	private final String expectedOneRow = "col-1;col-2\r\nhello;world\r\n";
	private Generator generator;
	private CsvConfiguration config;
	
	public GeneratorTest() {
		generator = new Generator();
		config = new CsvConfiguration();
		List<Column> columns = config.getColumns();
		Column c1 = new Column();
		c1.setColumnName("col-1");
		c1.setType(ColumnType.TEXT);
		columns.add(c1);
		Column c2 = new Column();
		c2.setColumnName("col-2");
		c2.setType(ColumnType.TEXT);
		columns.add(c2);
	} 
	
	@Test
	public void emptyCollection() throws IOException {
		assertEquals("col-1;col-2\r\n", rows2String(new ArrayList<>()));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void negativeOffsetThrowsException() throws IOException {
		try (InputStream is = oneRowInputStream()) {
			is.read(new byte[10], -1, 0);
		}
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void tooLargeOffsetThrowsException() throws IOException {
		try (InputStream is = oneRowInputStream()) {
			is.read(new byte[10], 11, 0);
		}
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void negativeLengthThrowsException() throws IOException {
		try (InputStream is = oneRowInputStream()) {
			is.read(new byte[10], 0, -1);
		}
	}

	@Test
	public void zeroLength() throws IOException {
		try (InputStream is = oneRowInputStream()) {
			assertEquals(0, is.read(new byte[10], 0, 0));
		}
	}

	@Test
	public void readByte() throws IOException {
		InputStream is = generator.generate(new ArrayList<>(), config);
		assertEquals('c', is.read());
		is.close();
	}

	@Test
	public void readWithNullBuffer() throws IOException {
		InputStream is = generator.generate(new ArrayList<>(), config);
		try {
			is.read(null, 0, 42);
			fail("NPE missing");
		} catch (NullPointerException e) {
			// expected
		}
		is.close();
	}

	@Test
	public void oneRowReadSingleBytes() throws IOException {
		InputStream is = oneRowInputStream();
		int ch = is.read();
		int i = 0;
		byte[] buffer = new byte[expectedOneRow.length()];
		while (ch != -1) {
			buffer[i++] = (byte) ch;
			ch = is.read();
		}
		is.close();
		assertEquals(expectedOneRow, new String(buffer, "Cp1252"));
	}

	@Test
	public void oneRowReadBytesInArray() throws IOException {
		InputStream is = oneRowInputStream();
		byte[] buffer = new byte[expectedOneRow.length()];
		is.read(buffer);
		is.close();
		assertEquals(expectedOneRow, new String(buffer, "Cp1252"));
	}

	@Test
	public void oneRow() throws IOException {
		InputStream is = oneRowInputStream();
		assertEquals(expectedOneRow, stream2String(is));
	}

	@Test
	public void twoRows() throws IOException {
		List<Map<String, String>> rows = new ArrayList<>();
		rows.add(row("hello", "world"));
		rows.add(row("ola", "munda"));
		assertEquals("col-1;col-2\r\nhello;world\r\nola;munda\r\n", rows2String(rows));
	}

	@Test
	public void largeCsv() throws IOException {
		List<Map<String, String>> rows = new ArrayList<>();
		StringBuilder expected = new StringBuilder(10_000);
		expected.append("col-1;col-2\r\n");
		for (int i = 0; i < 100_000; i++) {
			rows.add(row("a-" + i, "b-" + i));
			expected.append("a-" + i + ";b-" + i + "\r\n");
		}
		assertEquals(expected.toString(), rows2String(rows));
	}

	private InputStream oneRowInputStream() throws IOException {
		List<Map<String, String>> rows = new ArrayList<>();
		rows.add(row("hello", "world"));
		return generator.generate(rows, config);
	}

	private Map<String, String> row(String... values) {
		Map<String, String> r = new LinkedHashMap<>();
		for (int i = 0; i < values.length; i++) {
			r.put("col-" + (i + 1), values[i]);
		}
		return r;
	}

	private String rows2String(List<Map<String, String>> rows) throws IOException {
		return stream2String(generator.generate(rows, config));
	}

	private String stream2String(InputStream is) throws IOException {
		return IOUtils.toString(is, "Cp1252");
	}
}