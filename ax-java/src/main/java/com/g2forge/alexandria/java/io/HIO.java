package com.g2forge.alexandria.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HStream;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IThrowFunction1;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HIO {
	/**
	 * Read the contents of a input stream into a string. This method will close the stream when it is done.
	 * 
	 * @param stream The input stream to read.
	 * @param newline <code>true</code> will translate newlines from system format to <code>\n</code>
	 * @return The contents of the input stream.
	 */
	public static String readAll(final InputStream stream, boolean newline) {
		final String retVal;
		try (final Scanner scanner = new Scanner(stream, "UTF-8")) {
			retVal = scanner.useDelimiter("\\A").next();
		}
		return newline ? retVal.replace(System.lineSeparator(), "\n") : retVal;
	}

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

		try (final ICloseable closeStreams = () -> closeAll(streams)) {
			final List<ReadableByteChannel> channels = streams.stream().map(Channels::newChannel).collect(Collectors.toList());
			try (final ICloseable closeChannels = () -> closeAll(channels)) {
				final int bufferSize = getRecommendedBufferSize();
				final List<ByteBuffer> buffers = streams.stream().map(stream -> ByteBuffer.allocateDirect(bufferSize)).collect(Collectors.toList());
				while (true) {
					final List<Integer> counts = HStream.zip(channels.stream(), buffers.stream(), (channel, buffer) -> {
						try {
							return channel.read(buffer);
						} catch (IOException exception) {
							throw new RuntimeIOException(exception);
						}
					}).collect(Collectors.toList());
					// If any stream is done, then they're equals only if they're all don
					if (counts.stream().anyMatch(count -> count == -1)) return counts.stream().allMatch(count -> count == -1);

					// Compare all the buffer contents
					buffers.forEach(Buffer::flip);
					final ByteBuffer buffer = buffers.get(0);
					for (int j = 1; j < buffers.size(); j++) {
						if (!buffer.equals(buffers.get(j))) return false;
					}
					buffers.forEach(Buffer::clear);
				}
			}
		}
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
}
