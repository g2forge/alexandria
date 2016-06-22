package com.g2forge.alexandria.reflection.annotations;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.g2forge.alexandria.generic.type.java.structure.JavaScope;

public interface IJavaAnnotations {
	public <T extends Annotation> T getAnnotation(Class<T> type);

	public Collection<? extends Annotation> getAnnotations(JavaScope scope);

	public boolean isAnnotated(Class<? extends Annotation> type);
}
