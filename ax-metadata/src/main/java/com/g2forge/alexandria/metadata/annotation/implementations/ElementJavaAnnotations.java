package com.g2forge.alexandria.metadata.annotation.implementations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.metadata.annotation.IJavaAnnotations;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class ElementJavaAnnotations implements IJavaAnnotations {
	protected final AnnotatedElement annotated;

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		return getAnnotated().getAnnotation(type);
	}

	@Override
	public Collection<? extends Annotation> getAnnotations(JavaScope scope) {
		switch (scope) {
			case Instance:
			case Static:
				return HCollection.asList(getAnnotated().getDeclaredAnnotations());
			case Inherited:
				return HCollection.asList(getAnnotated().getAnnotations());
		}
		return null;
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		return getAnnotated().isAnnotationPresent(type);
	}
}
