package com.g2forge.alexandria.java.reflect.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class ValueJavaAnnotations implements IElementJavaAnnotations {
	protected final Object value;

	@Override
	public AnnotatedElement getAnnotatedElement() {
		final Object value = getValue();
		// If the value is an annotation, then we need to get the real type, since annotations are implemented by proxies
		if (value instanceof Annotation) return ((Annotation) value).annotationType();
		return value.getClass();
	}
}
