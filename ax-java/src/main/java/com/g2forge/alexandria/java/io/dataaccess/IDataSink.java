package com.g2forge.alexandria.java.io.dataaccess;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.type.ref.ITypeRef;

public interface IDataSink extends IDataAccess {
	@Note(type = NoteType.TODO, value = "Use static type switch", issue = "G2-432")
	public default <T extends OutputStream> T getStream(ITypeRef<T> type) {
		if ((type != null) && !OutputStream.class.equals(type.getErasedType())) throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		final T retVal = (T) Channels.newOutputStream(getChannel(ITypeRef.of(WritableByteChannel.class)));
		return retVal;
	}

	@Note(type = NoteType.TODO, value = "Use static type switch", issue = "G2-432")
	public default <T extends Writer> T getWriter(ITypeRef<T> type) {
		if ((type != null) && !Writer.class.equals(type.getErasedType())) throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		final T retVal = (T) Channels.newWriter(getChannel(ITypeRef.of(WritableByteChannel.class)), Charset.defaultCharset().newEncoder(), -1);
		return retVal;
	}

	public default void put(byte[] bytes) {
		try (final WritableByteChannel channel = getChannel(ITypeRef.of(WritableByteChannel.class))) {
			channel.write(ByteBuffer.wrap(bytes));
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}

	public default void put(String string) {
		try (final Writer writer = getWriter(null)) {
			writer.write(string);
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
