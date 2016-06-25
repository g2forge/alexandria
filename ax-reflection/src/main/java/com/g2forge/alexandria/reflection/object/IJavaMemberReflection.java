package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.member.IJavaMemberType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaTyped;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotated;

public interface IJavaMemberReflection<O> extends IJavaTyped, IJavaAnnotated {
	@Override
	public IJavaMemberType getType();
	
	public IJavaClassReflection<O> getDeclaringClass();
}
