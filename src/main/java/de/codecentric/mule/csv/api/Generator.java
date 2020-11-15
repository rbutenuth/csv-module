package de.codecentric.mule.csv.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;

/**
 * Implementation of operation "generate"
 */
public class Generator {

	@Throws(value = OperationErrorTypeProvider.class)
	@MediaType(value = "application/csv")
	public InputStream generate(@TypeResolver(InputObjectMetadataResolver.class) Iterable<Map<String, String>> rows, @Config CsvConfiguration config) throws IOException {
		return new CsvInputStream(rows, config);
	}
	
	private class CsvInputStream extends InputStream {
		private Iterator<Map<String, String>> rowIter;
		private ResettableByteArrayOutputStream out = new ResettableByteArrayOutputStream();
		CSVPrinter printer;

		public CsvInputStream(Iterable<Map<String, String>> rows, CsvConfiguration config) throws IOException {
			rowIter = rows.iterator();
			CSVFormat format = config.buildCsvFormat();
				OutputStreamWriter writer = new OutputStreamWriter(out, config.getCharset());
				printer = new CSVPrinter(writer, format);
				printer.flush();
		}

		@Override
		public int read() throws IOException {
			if (!fillBuffer(1)) {
				return -1;
			}
			return out.getByte();
		}

		@Override
		public int read(byte[] buffer, int offset, int length) throws IOException {
			if (buffer == null) {
				throw new NullPointerException();
			} else if (offset < 0 || length < 0 || length > buffer.length - offset) {
				throw new IndexOutOfBoundsException();
			} else if (length == 0) {
				return 0;
			}
			if (!fillBuffer(length)) {
				return -1;
			}
			return out.getBytes(buffer, offset, length);
		}

		private boolean fillBuffer(int bytesIWant) throws IOException {
			while (out.getSize() < bytesIWant && rowIter.hasNext()) {
				printer.printRecord(rowIter.next().values());
				printer.flush();
			}

			return out.getSize() > 0;
		}
	}

	private static class ResettableByteArrayOutputStream extends ByteArrayOutputStream {
		private int pos = 0;

		public int getSize() {
			return count - pos;
		}

		public int getByte() {
			byte b = buf[pos++];
			shiftIfNecessary();
			return b;
		}

		public int getBytes(byte[] buffer, int offset, int length) {
			int got = Math.min(length, getSize());
			System.arraycopy(buf, pos, buffer, offset, got);
			pos += got;
			shiftIfNecessary();
			return got;
		}

		/**
		 * When we have consumed at least 1000 bytes and at least one half from the buffer,
		 * shift the content to the beginning, making room at the end. This should keep
		 * the buffer small.
		 */
		private void shiftIfNecessary() {
			if (pos > 1000 && pos > buf.length / 2) {
				int size = getSize();
				System.arraycopy(buf, pos, buf, 0, size);
				count -= pos;
				pos = 0;
			}
		}
	}
}
