package com.g2forge.alexandria.java.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.g2forge.alexandria.java.marker.Helpers;

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
		final byte[] buffer = new byte[HIO.getRecommendedBufferSize()];
		while (true) {
			final int actual;
			try {
				actual = input.read(buffer);
			} catch (final IOException exception) {
				throw new RuntimeIOException(exception);
			}
			if (actual < 0) break;

			output.write(buffer, 0, actual);
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
