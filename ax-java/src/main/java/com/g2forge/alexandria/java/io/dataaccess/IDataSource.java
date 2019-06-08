package com.g2forge.alexandria.java.io.dataaccess;

import java.io.InputStream;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.java.io.HBinaryIO;
import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.type.ref.ITypeRef;

public interface IDataSource extends IDataAccess {
	public default byte[] getBytes() {
		return HBinaryIO.read(getStream(null));
	}

	@TODO(value = "Use static type switch", link = "G2-432")
	public default <T extends Reader> T getReader(ITypeRef<T> type) {
		if ((type != null) && !Reader.class.equals(type.getErasedType())) throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		final T retVal = (T) Channels.newReader(getChannel(ITypeRef.of(ReadableByteChannel.class)), Charset.defaultCharset().newDecoder(), -1);
		return retVal;
	}

	@TODO(value = "Use static type switch", link = "G2-432")
	public default <T extends InputStream> T getStream(ITypeRef<T> type) {
		if ((type != null) && !InputStream.class.equals(type.getErasedType())) throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		final T retVal = (T) Channels.newInputStream(getChannel(ITypeRef.of(ReadableByteChannel.class)));
		return retVal;
	}

	public default String getString() {
		return HIO.readAll(getStream(null), false);
	}
}
