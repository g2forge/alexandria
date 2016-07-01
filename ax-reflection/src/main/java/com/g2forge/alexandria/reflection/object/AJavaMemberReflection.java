package com.g2forge.alexandria.reflection.object;

import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.generic.type.java.member.IJavaMemberType;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.reflection.annotations.implementations.JavaAnnotations;
import com.g2forge.alexandria.reflection.object.implementations.JavaConcreteReflection;

public abstract class AJavaMemberReflection<O, MT extends IJavaMemberType> implements IJavaMemberReflection<O> {
	protected final MT type;

	/**
	 * @param type
	 */
	public AJavaMemberReflection(MT type) {
		this.type = type;
	}

	@Override
	public IJavaAnnotations getAnnotations() {
		return new JavaAnnotations((AnnotatedElement /* Cast is safe, no idea why Java standard library doesn't allow this */ ) getType().getJavaMember());
	}

	@Override
	public IJavaConcreteReflection<O> getDeclaringClass() {
		return new JavaConcreteReflection<>(type.getDeclaringClass());
	}

	@Override
	public MT getType() {
		return type;
	}
}
