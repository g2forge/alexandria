package com.g2forge.alexandria.metadata.v3.implementations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.metadata.v3.IJavaAnnotations;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ElementJavaAnnotations implements IJavaAnnotations {
	protected final AnnotatedElement annotated;

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		return annotated.getAnnotation(type);
	}

	@Override
	public Collection<? extends Annotation> getAnnotations(JavaScope scope) {
		switch (scope) {
			case Instance:
			case Static:
				return HCollection.asList(annotated.getDeclaredAnnotations());
			case Inherited:
				return HCollection.asList(annotated.getAnnotations());
		}
		return null;
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		return annotated.isAnnotationPresent(type);
	}
}
