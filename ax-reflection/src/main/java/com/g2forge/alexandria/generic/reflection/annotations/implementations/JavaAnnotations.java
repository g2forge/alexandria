package com.g2forge.alexandria.generic.reflection.annotations.implementations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import com.g2forge.alexandria.generic.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.generic.type.java.structure.JavaMembership;
import com.g2forge.alexandria.java.core.helpers.CollectionHelpers;

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
	public Collection<? extends Annotation> getAnnotations(JavaMembership membership) {
		switch (membership) {
			case Declared:
				return CollectionHelpers.asList(annotated.getDeclaredAnnotations());
			case All:
				return CollectionHelpers.asList(annotated.getAnnotations());
		}
		return null;
	}
	
	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		return annotated.isAnnotationPresent(type);
	}
}
