package com.g2forge.alexandria.metadata.v5.load;

import com.g2forge.alexandria.metadata.v5.IAnnotations;
import com.g2forge.alexandria.metadata.v5.IMetadata;

public interface IAnnotatedMetadata extends IMetadata, IAnnotations {
	@Override
	public default <T> T getMetadata(Class<T> type) {
		return IMetadataLoader.find(type).load(type, this);
	}

	@Override
	public default boolean isMetadataPresent(Class<?> type) {
		return IMetadataLoader.find(type).isPresent(type, this);
	}
}
