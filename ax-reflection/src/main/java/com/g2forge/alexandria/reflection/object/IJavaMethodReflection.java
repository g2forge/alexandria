package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;

public interface IJavaMethodReflection<T, O> extends IJavaMemberReflection<T> {
	public IJavaTypeReflection<O> getReturnType();

	@Override
	public IJavaMethodType getType();

	public O invoke(T object, Object... args);
}
