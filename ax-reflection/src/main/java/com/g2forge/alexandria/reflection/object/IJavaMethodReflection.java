package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.IJavaMethodType;

public interface IJavaMethodReflection<O, M> extends IJavaMemberReflection<O, M> {
	@Override
	public IJavaMethodType getType();
}
