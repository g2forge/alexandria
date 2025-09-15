package com.g2forge.alexandria.java.reflect.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAnnotatedElement implements AnnotatedElement {
	protected final Annotation[] annotations;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		for (Annotation element : this.annotations) {
			if (annotationClass.isInstance(element)) return (T) element;
		}
		return null;
	}

	@Override
	public Annotation[] getAnnotations() {
		return Arrays.copyOf(annotations, annotations.length);
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return getAnnotations();
	}
}
