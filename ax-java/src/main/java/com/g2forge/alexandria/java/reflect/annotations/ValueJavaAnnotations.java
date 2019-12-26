package com.g2forge.alexandria.java.reflect.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.reflect.JavaScope;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class ValueJavaAnnotations implements IJavaAnnotations {
	protected final Object value;

	protected AnnotatedElement getAnnotatedElement() {
		final Object value = getValue();
		// If the value is an annotation, then we need to get the real type, since annotations are implemented by proxies
		if (value instanceof Annotation) return ((Annotation) value).annotationType();
		return value.getClass();
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		return getAnnotatedElement().getAnnotation(type);
	}

	@Override
	public Collection<? extends Annotation> getAnnotations(JavaScope scope) {
		return HCollection.asList(scope.isInherited() ? getAnnotatedElement().getAnnotations() : getAnnotatedElement().getDeclaredAnnotations());
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		return getAnnotatedElement().isAnnotationPresent(type);
	}
}
