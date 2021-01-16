package com.g2forge.alexandria.java.io.dataaccess;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.type.ref.ITypeRef;

import lombok.Getter;

@Getter
public class PathDataSource implements IDataSource {
	protected final Path path;
	
	protected final OpenOption[] openOptions;

	public PathDataSource(Path path, OpenOption... openOptions) {
		this.path = path;
		this.openOptions = openOptions;
	}

	@Override
	public <T extends Channel> T getChannel(ITypeRef<T> type) {
		if ((type != null) && !type.isAssignableFrom(ITypeRef.of(SeekableByteChannel.class))) throw new IllegalArgumentException(String.format("Cannot generate channel type %1$s", type.getErasedType().getSimpleName()));
		try {
			@SuppressWarnings("unchecked")
			final T retVal = (T) Files.newByteChannel(getPath(), getOpenOptions());
			return retVal;
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	@Override
	public boolean isUsed() {
		return false;
	}
}