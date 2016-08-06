package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.member.IJavaConstructorType;

public interface IJavaConstructorReflection<T> extends IJavaMemberReflection<T> {
	@Override
	public IJavaConstructorType getType();

	public T newInstance(Object... args);
}
