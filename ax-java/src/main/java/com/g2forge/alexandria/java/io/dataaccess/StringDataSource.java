package com.g2forge.alexandria.java.io.dataaccess;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import com.g2forge.alexandria.java.type.ref.ITypeRef;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StringDataSource implements IDataSource {
	protected final String string;

	@Override
	public byte[] getBytes() {
		return getString().getBytes();
	}

	@Override
	public <T extends Channel> T getChannel(ITypeRef<T> type) {
		if ((type != null) && !ReadableByteChannel.class.equals(type.getErasedType())) throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		final T retVal = (T) Channels.newChannel(toByteArrayInputStream());
		return retVal;
	}

	@Override
	public Path getPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Reader> T getReader(ITypeRef<T> type) {
		if (type.isAssignableFrom(ITypeRef.of(StringReader.class))) {
			@SuppressWarnings({ "unchecked", "resource" })
			final T retVal = (T) new StringReader(getString());
			return retVal;
		}
		return IDataSource.getReader(this, type);
	}

	@Override
	public <T extends InputStream> T getStream(ITypeRef<T> type) {
		if (type.isAssignableFrom(ITypeRef.of(ByteArrayInputStream.class))) {
			@SuppressWarnings("unchecked")
			final T retVal = (T) toByteArrayInputStream();
			return retVal;
		}
		return IDataSource.getStream(this, type);
	}

	@Override
	public boolean isUsed() {
		return false;
	}

	protected ByteArrayInputStream toByteArrayInputStream() {
		return new ByteArrayInputStream(getBytes());
	}
}