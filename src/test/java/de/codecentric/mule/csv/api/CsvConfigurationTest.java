package de.codecentric.mule.csv.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.Test;

public class CsvConfigurationTest {

	@Test
	public void defaultConfiguration() {
		CsvConfiguration config = new CsvConfiguration();
		CSVFormat format = config.buildCsvFormat();
		assertEquals(';', format.getDelimiter());
		assertEquals(new Character('"'), format.getQuoteCharacter());
		assertEquals(new Character('"'), format.getEscapeCharacter());
		assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
		assertEquals("\r\n", format.getRecordSeparator());
		assertNull(format.getCommentMarker());
		assertFalse(format.getIgnoreSurroundingSpaces());
		assertFalse(format.getIgnoreEmptyLines());
		assertFalse(format.getTrim());
		assertFalse(format.getTrailingDelimiter());
		assertEquals("UTF-8", config.getCharset());
		assertEquals(0, config.getColumns().size());
		assertTrue(config.isWithHeaderLine());
	}
}
