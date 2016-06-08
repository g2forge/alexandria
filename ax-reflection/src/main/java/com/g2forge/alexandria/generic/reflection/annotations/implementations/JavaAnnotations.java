package com.g2forge.alexandria.generic.reflection.annotations.implementations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;

import com.g2forge.alexandria.generic.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.generic.type.java.structure.JavaMembership;

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
				return Arrays.asList(annotated.getDeclaredAnnotations());
			case All:
				return Arrays.asList(annotated.getAnnotations());
		}
		return null;
	}
	
	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		return annotated.isAnnotationPresent(type);
	}
}
