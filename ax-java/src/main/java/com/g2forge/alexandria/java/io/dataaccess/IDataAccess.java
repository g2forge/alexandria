package com.g2forge.alexandria.java.io.dataaccess;

import java.nio.channels.Channel;
import java.nio.file.Path;

import com.g2forge.alexandria.java.type.ref.ITypeRef;

public interface IDataAccess {
	public default void assertReady() {
		if (isUsed()) throw new IllegalStateException();
	}

	public <T extends Channel> T getChannel(ITypeRef<T> type);

	public Path getPath();

	public boolean isUsed();
}
