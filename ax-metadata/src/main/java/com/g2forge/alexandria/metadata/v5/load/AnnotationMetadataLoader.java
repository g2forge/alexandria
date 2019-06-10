package com.g2forge.alexandria.metadata.v5.load;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.metadata.v5.IMetadata;

public class AnnotationMetadataLoader implements IMetadataLoader, ISingleton {
	protected <T> AnnotatedElement getAnnotatedElement(Class<T> type, IMetadata element) {
		// Note we use the native annotation API here, since this can never be general metadata anyway
		final Retention retention = type.getAnnotation(Retention.class);
		if ((retention == null) || !RetentionPolicy.RUNTIME.equals(retention.value())) throw new IllegalArgumentException("The annotation \"" + type.getName() + "\" cannot be read at runtime, since it is not retained!");

		final AnnotatedElement _element = ((Metadata) element).getElement();
		return _element;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isPresent(Class<?> type, IMetadata element) {
		return getAnnotatedElement(type, element).isAnnotationPresent((Class) type);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T load(Class<T> type, IMetadata element) {
		return (T) getAnnotatedElement(type, element).getAnnotation((Class) type);
	}
}
