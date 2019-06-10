package com.g2forge.alexandria.metadata.v5.load;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.metadata.v5.IAnnotations;
import com.g2forge.alexandria.metadata.v5.IMetadata;

public class AnnotationMetadataLoader implements IMetadataLoader, ISingleton {
	protected void check(Class<?> type) {
		// Note we use the native annotation API here, since this can never be general metadata anyway
		final Retention retention = type.getAnnotation(Retention.class);
		if ((retention == null) || !RetentionPolicy.RUNTIME.equals(retention.value())) throw new IllegalArgumentException("The annotation \"" + type.getName() + "\" cannot be read at runtime, since it is not retained!");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isPresent(Class<?> type, IMetadata metadata) {
		check(type);
		return ((IAnnotations) metadata).isAnnotated((Class) type);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T load(Class<T> type, IMetadata metadata) {
		check(type);
		return (T) ((IAnnotations) metadata).getAnnotation((Class) type);
	}
}
