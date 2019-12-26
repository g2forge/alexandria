package com.g2forge.alexandria.java.reflect.annotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.reflect.JavaScope;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class MergedJavaAnnotations implements IJavaAnnotations {
	@Singular("annotations")
	protected final List<IJavaAnnotations> annotations;

	public MergedJavaAnnotations(IJavaAnnotations... annotations) {
		this(Arrays.asList(annotations));
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		for (IJavaAnnotations annotations : getAnnotations()) {
			final T retVal = annotations.getAnnotation(type);
			if (retVal != null) return retVal;
		}
		return null;
	}

	@Override
	public Collection<? extends Annotation> getAnnotations(final JavaScope scope) {
		return getAnnotations().stream().flatMap(annotations -> annotations.getAnnotations(scope).stream()).collect(Collectors.toList());
	}

	@Override
	public boolean isAnnotated(Class<? extends Annotation> type) {
		for (IJavaAnnotations annotations : getAnnotations()) {
			if (annotations.isAnnotated(type)) return true;
		}
		return false;
	}
}
