package com.g2forge.alexandria.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IConsumer1;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HTextIO {
	/**
	 * Read the contents of a input stream into a string. This method will close the stream when it is done.
	 * 
	 * @param stream The input stream to read.
	 * @param newline <code>true</code> will translate newlines from system format to <code>\n</code>
	 * @return The contents of the input stream.
	 */
	public static String readAll(final InputStream stream, boolean newline) {
		final StringBuilder retVal = new StringBuilder();

		final char[] buffer = new char[HIO.getRecommendedBufferSize()];
		try (final InputStreamReader reader = new InputStreamReader(stream, "UTF-8")) {
			while (true) {
				final int read = reader.read(buffer);
				if (read > 0) retVal.append(buffer, 0, read);
				if (read < 0) break;
			}
		} catch (IOException exception) {
			throw new RuntimeIOException(String.format("Failed to read stream %1$s to string", stream), exception);
		}
		return newline ? retVal.toString().replace(System.lineSeparator(), "\n") : retVal.toString();
	}

	public List<String> readAll(InputStream stream) {
		final List<String> retVal = new ArrayList<>();
		readAll(stream, retVal::add);
		return retVal;
	}

	public void readAll(InputStream stream, IConsumer1<? super String> consumer) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				consumer.accept(line);
			}
		} catch (IOException exception) {
			throw new RuntimeIOException(String.format("Failed to read stream %1$s to list of strings", stream), exception);
		}
	}

	public static boolean isEqual(InputStream... streams) {
		return isEqual(HCollection.asList(streams));
	}

	public static boolean isEqual(Collection<? extends InputStream> streams) {
		if (streams.size() <= 1) {
			if (streams.size() > 0) try {
				HCollection.getOne(streams).close();
			} catch (IOException exception) {
				throw new RuntimeIOException(exception);
			}
			return true;
		}

		try (final ICloseable closeStreams = () -> HIO.closeAll(streams)) {
			final List<BufferedReader> readers = streams.stream().map(InputStreamReader::new).map(BufferedReader::new).collect(Collectors.toList());
			try (final ICloseable closeReaders = () -> HIO.closeAll(readers)) {
				final Set<String> lines = readers.stream().map(reader -> {
					try {
						return reader.readLine();
					} catch (IOException e) {
						throw new RuntimeIOException(e);
					}
				}).collect(Collectors.toSet());
				if (lines.size() != 1) return false;
			}
		}

		return true;
	}
}
