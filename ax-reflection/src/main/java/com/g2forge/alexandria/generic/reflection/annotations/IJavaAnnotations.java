package com.g2forge.alexandria.generic.reflection.annotations;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.g2forge.alexandria.generic.type.java.structure.JavaMembership;

public interface IJavaAnnotations {
	public <T extends Annotation> T getAnnotation(Class<T> type);
	
	public Collection<? extends Annotation> getAnnotations(JavaMembership membership);
	
	public boolean isAnnotated(Class<? extends Annotation> type);
}
