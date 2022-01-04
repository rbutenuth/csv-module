package de.codecentric.mule.csv.api;

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

/**
 * Description of a CSV table. Used to control the parser and the metadata
 * generation. See {@link CSVFormat} for the meaning of the parameters.
 */
@Operations({ Parser.class, Generator.class })
public class CsvConfiguration {

	@Parameter
	@Optional(defaultValue = ";")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Delimiter")
	@Summary("Delimiter between two columns.")
	private String delimiter;

	@Parameter
	@Optional(defaultValue = "\"")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Quote Character")
	@Summary("Quote character before and after a column, needed when a column contains the delimiter character.")
	private String quoteChar;

	@Parameter
	@Optional(defaultValue = "")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Escape Character")
	@Summary("Needed when a column contains the quote character.")
	private String escape;
	
	@Parameter
	@Optional(defaultValue = "MINIMAL")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Quote Mode")
	@Summary("When is a column qouted? Relevant for CSV output only.")
	private QuoteMode quoteMode;

	@Parameter
	@Optional(defaultValue = "\\r\\n")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Record Separator")
	@Summary("String between two records, usually a new line.")
	private String recordSeparator;

	@Parameter
	@Optional
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Comment Start Character")
	@Summary("Start of a comment, leave emtpy when you don't want comment support.")
	private String commentStart;

	@Parameter
	@Optional(defaultValue = "false")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Ignore Surrounding Spaces")
	@Summary("")
	private boolean ignoreSurroundingSpaces;

	@Parameter
	@Optional(defaultValue = "false")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Ignore Empty Lines")
	@Summary("")
	private boolean ignoreEmptyLines;

	@Parameter
	@Optional(defaultValue = "false")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Trim")
	private boolean trim;

	@Parameter
	@Optional(defaultValue = "false")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Trailing Delimiter")
	@Summary("Is there a delimiter after the last column?")
	private boolean trailingDelimiter;

	@Parameter
	@Optional(defaultValue = "UTF-8")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Character Set")
	private String charset;

	@Parameter
	@Optional(defaultValue = "true")
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Starts with Header Line")
	@Summary("Does the file start with a header line containing the column names? When parsing, column names must matched specified column names.")
	private boolean withHeaderLine;

	@Parameter
	@ParameterDsl(allowReferences = false)
	@Expression(ExpressionSupport.NOT_SUPPORTED)
	@DisplayName("Columns")
	@Summary("Columns in the same order as in the file")
	private List<Column> columns = new ArrayList<>();

	public CsvConfiguration() {
		delimiter = ";";
		quoteChar = "\"";
		escape = "\"";
		quoteMode = QuoteMode.MINIMAL;
		recordSeparator = "\\r\\n";
		commentStart = null;
		ignoreSurroundingSpaces = false;
		ignoreEmptyLines = false;
		trim = false;
		trailingDelimiter = false;
		charset = "UTF-8";
		withHeaderLine = true;
	}
	
	public List<Column> getColumns() {
		return columns;
	}

	public String getCharset() {
		return charset;
	}
	public boolean isWithHeaderLine() {
		return withHeaderLine;
	}
	
	public CSVFormat buildCsvFormat() {
		CSVFormat format = CSVFormat.newFormat(unescapeFirst(delimiter)).withQuoteMode(quoteMode)
				.withIgnoreSurroundingSpaces(ignoreSurroundingSpaces).withIgnoreEmptyLines(ignoreEmptyLines)
				.withRecordSeparator(unescapeJava(recordSeparator)).withTrim(trim).withTrailingDelimiter(trailingDelimiter);
		if (StringUtils.isNotEmpty(quoteChar)) {
			format = format.withQuote(unescapeFirst(quoteChar));
		}
		if (StringUtils.isNotEmpty(escape)) {
			format = format.withEscape(unescapeFirst(escape));
		}
		if (StringUtils.isNotEmpty(commentStart)) {
			format = format.withCommentMarker(unescapeFirst(commentStart));
		}
		String[] headers = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			headers[i] = columns.get(i).getColumnName();
		}
		format = format.withHeader(headers);
		return format;
	}
	
	public Character unescapeFirst(String s) {
		return unescapeJava(s).charAt(0);
	}
}
