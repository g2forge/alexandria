package com.g2forge.alexandria.metadata.v5;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.g2forge.alexandria.java.reflect.JavaScope;

public interface IAnnotations {
	public <T extends Annotation> T getAnnotation(Class<T> type);

	public Collection<? extends Annotation> getAnnotations(JavaScope scope);

	public boolean isAnnotated(Class<? extends Annotation> type);
}
