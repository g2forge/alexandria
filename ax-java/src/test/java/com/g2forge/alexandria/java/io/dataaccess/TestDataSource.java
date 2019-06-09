package com.g2forge.alexandria.java.io.dataaccess;

import java.io.ByteArrayInputStream;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.java.type.ref.ITypeRef;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TestDataSource {
	@Getter
	@RequiredArgsConstructor
	public static class BufferedSourceData implements IDataSource {
		protected final ByteArrayInputStream stream;

		protected boolean used = false;

		@TODO(value = "Use static type switch", link = "G2-432")
		@Override
		public <T extends Channel> T getChannel(ITypeRef<T> type) {
			if ((type != null) && !ReadableByteChannel.class.equals(type.getErasedType())) throw new IllegalArgumentException();
			@SuppressWarnings("unchecked")
			final T retVal = (T) Channels.newChannel(stream);
			used = true;
			return retVal;
		}

		@Override
		public Path getPath() {
			throw new UnsupportedOperationException();
		}
	}

	@Test
	public void bytes() {
		final byte[] bytes = { 0x00, 0x01, 0x02 };
		final BufferedSourceData source = new BufferedSourceData(new ByteArrayInputStream(bytes));
		Assert.assertArrayEquals(bytes, source.getBytes());
	}

	@Test
	public void string() {
		final String string = "Hello, World!";
		final BufferedSourceData source = new BufferedSourceData(new ByteArrayInputStream(string.getBytes()));
		Assert.assertEquals(string, source.getString());
	}
}
