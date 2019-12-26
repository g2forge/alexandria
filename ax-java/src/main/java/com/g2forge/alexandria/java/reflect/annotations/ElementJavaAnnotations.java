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
public class ElementJavaAnnotations implements IJavaAnnotations {
	protected final AnnotatedElement annotatedElement;

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
