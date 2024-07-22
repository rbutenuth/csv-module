package de.codecentric.mule.csv.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.core.api.streaming.iterator.ConsumerStreamingIterator;

public class ParserTest extends MuleArtifactFunctionalTestCase {

	private static final int ROWS = 400;

	@Override
	protected String getConfigFile() {
		return "test-mule-config.xml";
	}

	@Test
	public void executeCsvToJava() throws Exception {
		InputStream payload = createCsvWithHeader();
		ConsumerStreamingIterator<?> result = (ConsumerStreamingIterator<?>) flowRunner("csv-to-java") //
				.withPayload(payload).run().getMessage().getPayload().getValue();

		checkResult(result);
	}

	@Test
	public void executeCsvToJavaWithManyRows() throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append("name|age|weight\r\n");
		for (int i = 0; i < ROWS; i++) {
			builder.append("name-").append(i).append('|');
			builder.append('"').append(1000 + i).append('"').append('|');
			builder.append(2000 + i).append('|');
			builder.append("\r\n");
		}
		InputStream payload = createCsv(builder.toString());
		@SuppressWarnings("unchecked")
		Iterator<Map<String, Object>> result = (Iterator<Map<String, Object>>) flowRunner("csv-to-java") //
				.withPayload(payload).run().getMessage().getPayload().getValue();
		int count = 0;
		while (result.hasNext()) {
			Map<String, Object> row = result.next();
			assertEquals("name-" + count, row.get("name"));
			assertEquals(Long.valueOf(1000 + count), row.get("age"));
			assertEquals(new BigDecimal(2000 + count), row.get("weight"));
			count++;
		}
		assertEquals(ROWS, count);
	}
	
	@Test
	public void executeCsvToJavaNoHeader() throws Exception {
		InputStream payload = createCsv("Max Mule|12|79.8\r\n");
		ConsumerStreamingIterator<?> result = (ConsumerStreamingIterator<?>) flowRunner("csv-to-java-no-header") //
				.withPayload(payload).run().getMessage().getPayload().getValue();

		checkResult(result);
	}

	@Test
	public void exceptionOnCloseIsIgnored() throws Exception {
		InputStream originalStream = createCsvWithHeader();
		InputStream payload = new InputStream() {
			
			@Override
			public int read() throws IOException {
				return originalStream.read();
			}
			
			@Override
			public void close() throws IOException {
				throw new IOException("should be ignored");
			}
		};
				
		ConsumerStreamingIterator<?> result = (ConsumerStreamingIterator<?>) flowRunner("csv-to-java") //
				.withPayload(payload).run().getMessage().getPayload().getValue();

		checkResult(result);
	}
	private void checkResult(ConsumerStreamingIterator<?> result) {
		assertEquals(-1, result.getSize());
		assertTrue(result.hasNext());
		@SuppressWarnings("unchecked")
		Map<String, Object> row = (Map<String, Object>) result.next();
		assertEquals(3, row.size());
		assertEquals("Max Mule", row.get("name"));
		assertEquals(Long.valueOf(12), row.get("age"));
		assertEquals(new BigDecimal("79.8"), row.get("weight"));
		assertFalse(result.hasNext());
	}

	@Test
	public void executeCsvToJavaMissingHeaderLine() throws Exception {
		InputStream payload = createCsv("");
		try {
			flowRunner("csv-to-java").withPayload(payload).run().getMessage().getPayload().getValue();
			fail("Exception missing");
		} catch (Exception e) {
			assertEquals("Missing header line.", e.getMessage());
		}
	}

	@Test
	public void executeCsvToJavaMissingHeaderColumn() throws Exception {
		InputStream payload = createCsv("Max Mule|12|79.8\r\n");
		try {
			flowRunner("csv-to-java").withPayload(payload).run().getMessage().getPayload().getValue();
			fail("Exception missing");
		} catch (Exception e) {
			assertEquals("Column 'name' excepted, but got 'Max Mule'.", e.getMessage());
		}
	}

	@Test
	public void executeCsvToJavaShortHeader() throws Exception {
		InputStream payload = createCsv("name|weight\r\n" + "Max Mule|12|79.8\r\n");
		try {
			flowRunner("csv-to-java").withPayload(payload).run().getMessage().getPayload().getValue();
			fail("Exception missing");
		} catch (Exception e) {
			assertEquals("Header should contain 3 columns, but contains 2.", e.getMessage());
		}
	}

	@Test
	public void executeCsvToJavaWrongHeaderName() throws Exception {
		InputStream payload = createCsv("name|age|length\r\n" + "Max Mule|12|79.8\r\n");
		try {
			flowRunner("csv-to-java").withPayload(payload).run().getMessage().getPayload().getValue();
			fail("Exception missing");
		} catch (Exception e) {
			assertEquals("Column 'weight' excepted, but got 'length'.", e.getMessage());
		}
	}

	
	
	private InputStream createCsvWithHeader() {
		String content = "name|age|weight\r\n" + "Max Mule|12|79.8\r\n";
		return createCsv(content);
	}

	private InputStream createCsv(String content) {
		return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
	}
}
