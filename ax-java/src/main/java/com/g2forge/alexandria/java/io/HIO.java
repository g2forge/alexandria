package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.helpers.HStream;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HIO {
	public static int getRecommendedBufferSize() {
		return 16384;
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
		if (streams.length <= 1) {
			if (streams.length > 0) try {
				streams[0].close();
			} catch (IOException exception) {
				throw new RuntimeIOException(exception);
			}
			return true;
		}

		final List<InputStream> list = Arrays.asList(streams);
		try {
			final List<ReadableByteChannel> channels = list.stream().map(Channels::newChannel).collect(Collectors.toList());
			try {
				final int bufferSize = getRecommendedBufferSize();
				final List<ByteBuffer> buffers = list.stream().map(stream -> ByteBuffer.allocateDirect(bufferSize)).collect(Collectors.toList());
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
			} finally {
				closeAll(channels);
			}
		} finally {
			closeAll(list);
		}
	}
}
