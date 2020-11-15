package de.codecentric.mule.csv.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.core.api.streaming.iterator.ConsumerStreamingIterator;

public class ParserTest extends MuleArtifactFunctionalTestCase {

	@Override
	protected String getConfigFile() {
		return "test-mule-config.xml";
	}

	@Test
	public void executeCsvToJava() throws Exception {
		InputStream payload = createCsv();
		ConsumerStreamingIterator<?> result = (ConsumerStreamingIterator<?>) flowRunner("csv-to-java") //
				.withPayload(payload).run().getMessage().getPayload().getValue();
		
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

	private InputStream createCsv() {
		String content = "name|age|weight\r\n" + "Max Mule|12|79.8\r\n";
		return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
	}
}
