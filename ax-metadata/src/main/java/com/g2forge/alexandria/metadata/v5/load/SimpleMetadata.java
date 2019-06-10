package com.g2forge.alexandria.metadata.v5.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.reflect.JavaScope;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SimpleMetadata implements IAnnotatedMetadata {
	protected final AnnotatedElement element;

	protected final Object value;

	@TODO(value = "Implement support for loading instance specific metadata", link = "G2-469")
	public SimpleMetadata(AnnotatedElement element) {
		this(element, null);
	}

	public SimpleMetadata(AnnotatedElement element, Object value) {
		if ((element == null) && (value != null)) {
			this.element = (value instanceof Annotation) ? ((Annotation) value).annotationType() : value.getClass();
			this.value = value;
		} else {
			this.element = element;
			this.value = value;
		}
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		return getElement().getAnnotation(type);
	}

	@Override
	public Collection<? extends Annotation> getAnnotations(JavaScope scope) {
		switch (scope) {
			case Instance:
			case Static:
				return HCollection.asList(getElement().getDeclaredAnnotations());
			case Inherited:
				return HCollection.asList(getElement().getAnnotations());
		}
		return null;
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		return getElement().isAnnotationPresent(type);
	}
}
