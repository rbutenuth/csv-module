package de.codecentric.mule.csv.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.mule.runtime.extension.api.exception.ModuleException;

public class ColumnTypeTest {

	@Test
	public void parseText() {
		assertEquals(ColumnType.TEXT.parse("foo", true), "foo");
		assertNull(ColumnType.TEXT.parse("", true));
		assertEquals(ColumnType.TEXT.parse("", false), "");
	}
	
	@Test
	public void parseLong() {
		assertEquals(ColumnType.INTEGER.parse("42", true), Long.valueOf(42));
		assertEquals(ColumnType.INTEGER.parse("42", false), Long.valueOf(42));
		assertNull(ColumnType.INTEGER.parse("", true));
		assertEquals(ColumnType.INTEGER.parse("", false), Long.valueOf(0));
		try {
			ColumnType.INTEGER.parse("4x2", true);
			fail("exception missing");
		} catch (ModuleException e) {
			assertEquals("Unparsable long: \"4x2\"", e.getMessage());
		}
	}
	
	@Test
	public void parseBigDecimal() {
		assertEquals(ColumnType.NUMBER.parse("3.14", true), BigDecimal.valueOf(314, 2));
		assertEquals(ColumnType.NUMBER.parse("3.14", false), BigDecimal.valueOf(314, 2));
		assertNull(ColumnType.NUMBER.parse("", true));
		assertEquals(ColumnType.NUMBER.parse("", false), BigDecimal.ZERO);
		try {
			ColumnType.NUMBER.parse("4x2", true);
			fail("exception missing");
		} catch (ModuleException e) {
			assertEquals("Unparsable BigInteger: \"4x2\"", e.getMessage());
		}
	}
	
	@Test
	public void generate() {
		assertEquals(ColumnType.TEXT.generate("foo"), "foo");
		assertEquals(ColumnType.TEXT.generate(null), "");
	}
}
