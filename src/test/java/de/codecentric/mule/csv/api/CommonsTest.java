package de.codecentric.mule.csv.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.junit.Test;

public class CommonsTest {

	@Test
	public void simpleGenerate() throws Exception {
		CSVFormat format = CSVFormat.EXCEL.builder().setHeader("H1", "H2").setQuoteMode(QuoteMode.NON_NUMERIC).build();
		// System.out.println("quote character: '" + format.getQuoteCharacter() + "'");
		// System.out.println("escape character: '" + format.getEscapeCharacter() +
		// "'");
		String formatted = format.format("a\"bc", 42);
		// System.out.println(formatted);
		StringReader rd = new StringReader(formatted);
		CSVParser parser = format.parse(rd);
		parser.getHeaderMap();
		Iterator<CSVRecord> iterator = parser.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			CSVRecord record = iterator.next();
			// System.out.println("H1:" + record.get(0));
			// System.out.println("H2:" + record.get(1));
			if (i == 0) {
				assertEquals("H1", record.get(0));
				assertEquals("H2", record.get(1));
			} else if (i == 1) {
				assertEquals("a\"bc", record.get(0));
				assertEquals("42", record.get(1));
			} else {
				fail("too many rows");
			}
			i++;
		}
		assertEquals(2, i);
		parser.close();
	}
}
