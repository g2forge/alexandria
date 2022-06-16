package com.g2forge.alexandria.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.stream.StreamSupport;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IThrowFunction1;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HIO {
	public static void close(AutoCloseable closeable) {
		try {
			closeable.close();
		} catch (Throwable throwable) {
			HError.throwQuietly(throwable);
		}
	}

	public static void closeAll(AutoCloseable... closeables) {
		closeAll(HCollection.asList(closeables));
	}

	public static void closeAll(Iterable<? extends AutoCloseable> closeables) {
		StreamSupport.stream(closeables.spliterator(), false).map(c -> HError.wrap(() -> c.close()).get()).collect(HError.collector(() -> new RuntimeIOException("Failed to close one or more closeables!"), false));
	}

	public static int getRecommendedBufferSize() {
		return 16384;
	}

	public static byte[] sha1(String value) {
		return HashAlgorithm.SHA_1.hash(value);
	}

	public static <T> byte[] sha1(T value, IThrowFunction1<T, InputStream, IOException> open) {
		return HashAlgorithm.SHA_1.hash(value, open);
	}

	public static InputStream toInputStream(String string) {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (final PrintStream print = new PrintStream(output)) {
			print.print(string);
		}
		return new ByteArrayInputStream(output.toByteArray());
	}
}
