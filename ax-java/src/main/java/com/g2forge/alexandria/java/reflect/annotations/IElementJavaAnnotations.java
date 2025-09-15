package com.g2forge.alexandria.java.reflect.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.reflect.JavaScope;

public interface IElementJavaAnnotations extends IJavaAnnotations {
	public AnnotatedElement getAnnotatedElement();

	@Override
	public default <T extends Annotation> T getAnnotation(Class<T> type) {
		return getAnnotatedElement().getAnnotation(type);
	}

	@Override
	public default Collection<? extends Annotation> getAnnotations(JavaScope scope) {
		return HCollection.asList(scope.isInherited() ? getAnnotatedElement().getAnnotations() : getAnnotatedElement().getDeclaredAnnotations());
	}

	@Override
	public default boolean isAnnotated(Class<? extends Annotation> type) {
		return getAnnotatedElement().isAnnotationPresent(type);
	}
}
