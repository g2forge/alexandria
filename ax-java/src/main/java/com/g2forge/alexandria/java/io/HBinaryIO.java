package com.g2forge.alexandria.java.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HStream;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IRunnable;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HBinaryIO {
	public static int checkMagicAndGetVersion(final InputStream input, final byte[] magic) {
		if (!Arrays.equals(read(input, magic.length), magic)) throw new IllegalDataException("Stored data lacked the magic header, perhaps it's a different object type!");
		return readInt(input);
	}

	/**
	 * Copy an input stream to an output stream.
	 * 
	 * @param input The input stream to read from.
	 * @param output The output stream to write to.
	 * @throws IOException IOException if an I/O error occurs. In particular, an <code>IOException</code> is thrown if the output stream is closed.
	 */
	public static void copy(final InputStream input, final OutputStream output) throws IOException {
		copy(input, output, HIO.getRecommendedBufferSize(), null);
	}

	public static void copy(final InputStream input, final OutputStream output, final int bufferSize, final IRunnable post) throws IOException {
		final byte[] buffer = new byte[bufferSize];
		while (true) {
			final int actual;
			try {
				actual = input.read(buffer);
			} catch (final IOException exception) {
				throw new RuntimeIOException(exception);
			}
			if (actual < 0) break;

			output.write(buffer, 0, actual);
			if (post != null) post.run();
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
			final List<ReadableByteChannel> channels = streams.stream().map(Channels::newChannel).collect(Collectors.toList());
			try (final ICloseable closeChannels = () -> HIO.closeAll(channels)) {
				final int bufferSize = HIO.getRecommendedBufferSize();
				final List<ByteBuffer> buffers = streams.stream().map(stream -> ByteBuffer.allocateDirect(bufferSize)).collect(Collectors.toList());
				while (true) {
					final List<Integer> counts = HStream.zip(channels.stream(), buffers.stream(), (channel, buffer) -> {
						try {
							return channel.read(buffer);
						} catch (IOException exception) {
							throw new RuntimeIOException(exception);
						}
					}).collect(Collectors.toList());
					// If any stream is done, then they're equals only if they're all done
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

	public static byte[] read(final InputStream input) {
		int total = 0;
		final List<byte[]> buffers = new ArrayList<>();
		while (true) {
			final byte[] buffer = new byte[HIO.getRecommendedBufferSize()];
			final int actual;
			try {
				actual = input.read(buffer);
			} catch (final IOException exception) {
				throw new RuntimeIOException(exception);
			}
			if (actual < 0) break;

			total += actual;
			if (actual != buffer.length) buffers.add(Arrays.copyOf(buffer, actual));
			else buffers.add(buffer);
		}

		final byte[] retVal = new byte[total];
		int offset = 0;
		for (byte[] buffer : buffers) {
			System.arraycopy(buffer, 0, retVal, offset, buffer.length);
			offset += buffer.length;
		}
		return retVal;
	}

	public static byte[] read(final InputStream input, final int length) {
		if (length < 0) throw new IllegalArgumentException("Cannot read fewer than zero bytes (" + length + ")!");
		final byte[] retVal = new byte[length];
		if (length > 0) {
			final int actual;
			try {
				actual = input.read(retVal);
			} catch (final IOException exception) {
				throw new RuntimeIOException(exception);
			}
			if (actual != length) throw new RuntimeIOException(new IOException("Unable to read " + length + "B, got " + actual + "B instead"));
		}
		return retVal;
	}

	public static byte readByte(final InputStream input) {
		final int retVal;
		try {
			retVal = input.read();
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
		if (retVal == -1) throw new RuntimeIOException(new EOFException());
		return (byte) retVal;
	}

	public static int readInt(final InputStream input) {
		final int length = Integer.SIZE / Byte.SIZE;
		final byte[] bytes = read(input, length);
		// Little endian!
		int retVal = 0;
		for (int i = 0; i < length; i++) {
			retVal |= (bytes[i]) << (Byte.SIZE * i);
		}
		return retVal;
	}

	public static <T extends OutputStream> T write(final T output, final byte[] data) {
		try {
			output.write(data);
		} catch (final IOException exception) {
			throw new RuntimeIOException(exception);
		}
		return output;
	}

	public static <T extends OutputStream> T write(final T output, final int value) {
		final int length = Integer.SIZE / Byte.SIZE;
		final byte[] bytes = new byte[length];
		// Little endian!
		for (int i = 0; i < length; i++) {
			bytes[i] = (byte) ((value >>> (Byte.SIZE * i)) & 0xFF);
		}
		return write(output, bytes);
	}
}
