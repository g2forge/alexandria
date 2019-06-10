package com.g2forge.alexandria.metadata.v5.load;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.metadata.v5.IMetadata;
import com.g2forge.alexandria.metadata.v5.MetadataLoader;

public interface IMetadataLoader {
	@TODO(value = "Support for non-annotation metadata", link = "G2-469")
	public static <T> IMetadataLoader find(Class<T> type) {
		if (MetadataLoader.class.isAssignableFrom(type)) return new AnnotatedMetadataLoader();

		final MetadataLoader metadata = IMetadata.of(type).getMetadata(MetadataLoader.class);
		if (metadata != null) {
			try {
				return metadata.value().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return new AnnotatedMetadataLoader();
	}

	public <T> T load(Class<T> type, IMetadata element);
}
