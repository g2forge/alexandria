package com.g2forge.alexandria.metadata.v4;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AFilteredAnnotatedElement implements AnnotatedElement {
	protected final AnnotatedElement element;

	protected abstract boolean filter(Annotation annotation);

	protected <T extends Annotation> T[] filter(final T[] annotations, IntFunction<T[]> constructor) {
		return Stream.of(annotations).filter(this::filter).toArray(constructor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		for (Annotation element : getAnnotations()) {
			if (annotationClass.isInstance(element)) return (T) element;
		}
		return null;
	}

	@Override
	public Annotation[] getAnnotations() {
		return filter(element.getAnnotations(), size -> new Annotation[size]);
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return filter(element.getDeclaredAnnotations(), size -> new Annotation[size]);
	}

	@SuppressWarnings("unchecked")
	public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
		return filter(element.getDeclaredAnnotationsByType(annotationClass), size -> (T[]) Array.newInstance(annotationClass, size));
	}
}
