package com.g2forge.alexandria.metadata.load;

import com.g2forge.alexandria.metadata.IMetadata;
import com.g2forge.alexandria.metadata.annotation.IJavaAnnotated;

public interface IAnnotatedMetadata extends IMetadata, IJavaAnnotated {
	@Override
	public default <T> T getMetadata(Class<T> type) {
		return IMetadataLoader.find(type).load(type, this);
	}

	@Override
	public default boolean isMetadataPresent(Class<?> type) {
		return IMetadataLoader.find(type).isPresent(type, this);
	}
}
