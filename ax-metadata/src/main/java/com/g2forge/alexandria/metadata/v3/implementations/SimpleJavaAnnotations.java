package com.g2forge.alexandria.metadata.v3.implementations;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.metadata.v3.IJavaAnnotations;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class SimpleJavaAnnotations implements IJavaAnnotations {
	protected final Collection<Annotation> annotations;

	public SimpleJavaAnnotations(Annotation... annotations) {
		this(HCollection.asList(annotations));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		for (Annotation element : annotations) {
			if (type.isInstance(element)) return (T) element;
		}
		return null;
	}

	@Override
	public Collection<? extends Annotation> getAnnotations(JavaScope scope) {
		return Collections.unmodifiableCollection(annotations);
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		return getAnnotation(type) != null;
	}
}
