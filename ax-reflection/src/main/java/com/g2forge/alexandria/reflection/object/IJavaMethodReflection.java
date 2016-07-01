package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;

public interface IJavaMethodReflection<O> extends IJavaMemberReflection<O> {
	@Override
	public IJavaMethodType getType();
}
