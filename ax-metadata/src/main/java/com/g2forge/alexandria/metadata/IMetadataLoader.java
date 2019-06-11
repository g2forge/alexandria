package com.g2forge.alexandria.metadata;

import com.g2forge.alexandria.java.function.IFunction1;

public interface IMetadataLoader {
	@SuppressWarnings("unchecked")
	public static <T, U> T load(Class<T> type, IMetadata metadata, Class<U> expected, IFunction1<? super IMetadata, ? extends U> loader) {
		if (!expected.equals(type)) throw new IllegalArgumentException();
		return (T) loader.apply(metadata);
	}

	public default boolean isPresent(Class<?> type, IMetadata metadata) {
		return load(type, metadata) != null;
	}

	public <T> T load(Class<T> type, IMetadata metadata);
}
