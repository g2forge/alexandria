package com.g2forge.alexandria.reflection.object;

import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.generic.type.java.member.IJavaMemberType;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.reflection.annotations.implementations.JavaAnnotations;
import com.g2forge.alexandria.reflection.object.implementations.JavaConcreteReflection;

import lombok.Data;

@Data
public abstract class AJavaMemberReflection<T, MT extends IJavaMemberType> implements IJavaMemberReflection<T> {
	protected final MT type;

	@Override
	public IJavaAnnotations getAnnotations() {
		return new JavaAnnotations((AnnotatedElement /* Cast is safe, no idea why Java standard library doesn't allow this */ ) getType().getJavaMember());
	}

	@Override
	public IJavaConcreteReflection<T> getDeclaringClass() {
		return new JavaConcreteReflection<>(type.getDeclaringClass());
	}
}
