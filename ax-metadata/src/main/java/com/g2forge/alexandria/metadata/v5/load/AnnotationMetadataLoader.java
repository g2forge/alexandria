package com.g2forge.alexandria.metadata.v5.load;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.metadata.v5.IMetadata;

public class AnnotationMetadataLoader implements IMetadataLoader, ISingleton {
	@Override
	public <T> T load(Class<T> type, IMetadata element) {
		final Retention retention = type.getAnnotation(Retention.class);
		if ((retention == null) || !RetentionPolicy.RUNTIME.equals(retention.value())) throw new IllegalArgumentException("The annotation \"" + type.getName() + "\" cannot be read at runtime, since it is not retained!");

		final AnnotatedElement _element = ((Metadata) element).getElement();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final T retVal = (T) _element.getAnnotation((Class) type);
		return retVal;
	}
}
