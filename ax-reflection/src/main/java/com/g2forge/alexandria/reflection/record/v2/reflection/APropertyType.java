package com.g2forge.alexandria.reflection.record.v2.reflection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.metadata.v5.annotation.IJavaAnnotated;
import com.g2forge.alexandria.metadata.v5.annotation.IJavaAnnotations;
import com.g2forge.alexandria.metadata.v5.annotation.implementations.MergedJavaAnnotations;
import com.g2forge.alexandria.reflection.record.v2.IPropertyType;

abstract class APropertyType<P> implements IPropertyType<P> {
	protected List<IJavaAnnotated> annotated = null;

	protected abstract IJavaAnnotated getAnnotated();

	protected void setOverride(APropertyType<?> previous) {
		annotated = new ArrayList<>();
		if (previous.annotated != null) annotated.addAll(previous.annotated);
		else annotated.add(previous.getAnnotated());
		annotated.add(getAnnotated());
	}

	@Override
	public IJavaAnnotations getAnnotations() {
		if (annotated == null) return getAnnotated().getAnnotations();
		return new MergedJavaAnnotations(annotated.stream().map(IJavaAnnotated::getAnnotations).collect(Collectors.toList()));
	}
}
