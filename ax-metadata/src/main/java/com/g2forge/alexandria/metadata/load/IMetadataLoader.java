package com.g2forge.alexandria.metadata.load;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.metadata.IMetadata;
import com.g2forge.alexandria.metadata.MetadataLoader;

public interface IMetadataLoader {
	@TODO(value = "Support for non-annotation metadata", link = "G2-469")
	public static <T> IMetadataLoader find(Class<T> type) {
		if (MetadataLoader.class.isAssignableFrom(type)) return new AnnotationMetadataLoader();

		final MetadataLoader metadata = IMetadata.of(type).getMetadata(MetadataLoader.class);
		if (metadata != null) {
			try {
				return metadata.value().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return new AnnotationMetadataLoader();
	}

	public default boolean isPresent(Class<?> type, IMetadata metadata) {
		return load(type, metadata) != null;
	}

	public <T> T load(Class<T> type, IMetadata metadata);
}
