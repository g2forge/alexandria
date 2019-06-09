package com.g2forge.alexandria.metadata.v3.implementations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.metadata.v3.IJavaAnnotations;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class MergedJavaAnnotations implements IJavaAnnotations {
	protected final Collection<IJavaAnnotations> annotations;

	public MergedJavaAnnotations(IJavaAnnotations... annotations) {
		this(Arrays.asList(annotations));
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		for (IJavaAnnotations annotations : this.annotations) {
			final T retVal = annotations.getAnnotation(type);
			if (retVal != null) return retVal;
		}
		return null;
	}

	@Override
	public Collection<? extends Annotation> getAnnotations(final JavaScope scope) {
		return annotations.stream().flatMap(annotations -> annotations.getAnnotations(scope).stream()).collect(Collectors.toList());
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		for (IJavaAnnotations annotations : this.annotations) {
			if (annotations.isAnnotated(type)) return true;
		}
		return false;
	}
}
