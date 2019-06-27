package com.g2forge.alexandria.java.io.dataaccess;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.type.ref.ITypeRef;

import lombok.Getter;

public class TestDataSink {
	@Getter
	public static class BufferedSinkData implements IDataSink {
		protected final ByteArrayOutputStream stream = new ByteArrayOutputStream();

		protected boolean used = false;

		@Note(type = NoteType.TODO, value = "Use static type switch", issue = "G2-432")
		@Override
		public <T extends Channel> T getChannel(ITypeRef<T> type) {
			if ((type != null) && !WritableByteChannel.class.equals(type.getErasedType())) throw new IllegalArgumentException();
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
		final BufferedSinkData sink = new BufferedSinkData();
		final byte[] bytes = { 0x00, 0x01, 0x02 };
		sink.put(bytes);
		Assert.assertArrayEquals(bytes, sink.getStream().toByteArray());
	}

	@Test
	public void string() {
		final BufferedSinkData sink = new BufferedSinkData();
		final String string = "Hello, World!";
		sink.put(string);
		Assert.assertEquals(string, sink.getStream().toString());
	}
}
