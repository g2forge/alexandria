package com.g2forge.alexandria.metadata.v5;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HMetadata {
	public static IElement access(AnnotatedElement element) {
		return new AnnotatedElementElement(element);
	}

	@TODO(value = "Support for non-annotation metadata", link = "G2-469")
	public static <T> IMetadataLoader<T> findLoader(Class<T> type) {
		if (type.isAnnotation()) {
			// Notice we don't use our own API here, since that would be an infinite loop
			final Retention retention = type.getAnnotation(Retention.class);
			if ((retention == null) || !RetentionPolicy.RUNTIME.equals(retention.value())) throw new IllegalArgumentException("The annotation \"" + type.getName() + "\" cannot be read at runtime, since it is not retained!");
			return element -> {
				final AnnotatedElement _element = ((AnnotatedElementElement) element).getElement();
				@SuppressWarnings({ "rawtypes", "unchecked" })
				final T retVal = (T) _element.getAnnotation((Class) type);
				return retVal;
			};
		}
		throw new IllegalArgumentException("Can't load non-annotation metadata yet!");
	}
}
