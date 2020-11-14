package de.codecentric.mule.csv.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.core.api.streaming.iterator.ConsumerStreamingIterator;

public class ParseTest extends MuleArtifactFunctionalTestCase {

	@Override
	protected String getConfigFile() {
		return "test-mule-config.xml";
	}

	@Test
	public void executeCsvToJava() throws Exception {
		InputStream payload = createCsv();
		ConsumerStreamingIterator<?> result = (ConsumerStreamingIterator<?>) flowRunner("csv-to-java") //
				.withPayload(payload).run().getMessage().getPayload().getValue();
		
		System.out.println("------------------" + result.getClass());
//		assertEquals(1, result.getSize());
		assertTrue(result.hasNext());
		System.out.println("------------------next=" + result.next());
		
	}

	private InputStream createCsv() {
		String content = "name|age|weight\r\n" + "Max Mule||79.8\r\n";
		return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
	}
}
