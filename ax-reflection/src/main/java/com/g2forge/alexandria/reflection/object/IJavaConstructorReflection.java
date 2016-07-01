package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.member.IJavaConstructorType;

public interface IJavaConstructorReflection<O> extends IJavaMemberReflection<O> {
	@Override
	public IJavaConstructorType getType();

	public O newInstance(Object... args);
}
