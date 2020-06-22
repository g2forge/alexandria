package com.g2forge.alexandria.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IThrowFunction1;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HIO {
	public static InputStream toInputStream(String string) {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (final PrintStream print = new PrintStream(output)) {
			print.print(string);
		}
		return new ByteArrayInputStream(output.toByteArray());
	}

	public static int getRecommendedBufferSize() {
		return 16384;
	}

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
		final Collection<Throwable> throwables = new ArrayList<>();
		for (AutoCloseable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (Throwable throwable) {
					throwables.add(throwable);
				}
			}
		}
		if (!throwables.isEmpty()) throw HError.addSuppressed(new RuntimeIOException("Failed to close one or more closeables!"), throwables);
	}

	public static <T> byte[] sha1(T value, IThrowFunction1<T, InputStream, IOException> open) {
		final MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		try (final InputStream stream = open.apply(value)) {
			final byte[] buffer = new byte[getRecommendedBufferSize()];
			int n = 0;
			while (n != -1) {
				n = stream.read(buffer);
				if (n > 0) {
					digest.update(buffer, 0, n);
				}
			}
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		return digest.digest();
	}

	public static byte[] sha1(String value) {
		return sha1(value.getBytes(), ByteArrayInputStream::new);
	}
}
