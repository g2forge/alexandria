package com.g2forge.alexandria.reflection.annotations.implementations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotations;

public class JavaAnnotations implements IJavaAnnotations {
	protected final AnnotatedElement annotated;

	/**
	 * @param annotated
	 */
	public JavaAnnotations(AnnotatedElement annotated) {
		this.annotated = annotated;
	}

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
