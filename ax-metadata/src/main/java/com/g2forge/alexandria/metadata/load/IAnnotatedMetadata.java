package com.g2forge.alexandria.metadata.load;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.metadata.IMetadata;
import com.g2forge.alexandria.metadata.IMetadataLoader;
import com.g2forge.alexandria.metadata.MetadataLoader;
import com.g2forge.alexandria.metadata.annotation.IJavaAnnotated;

public interface IAnnotatedMetadata extends IMetadata, IJavaAnnotated {
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

	@Override
	public default <T> T getMetadata(Class<T> type) {
		return IAnnotatedMetadata.find(type).load(type, this);
	}

	@Override
	public default boolean isMetadataPresent(Class<?> type) {
		return IAnnotatedMetadata.find(type).isPresent(type, this);
	}
}
