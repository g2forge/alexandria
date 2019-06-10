package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.member.IJavaMemberType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaTyped;
import com.g2forge.alexandria.metadata.annotation.IJavaAnnotated;

public interface IJavaMemberReflection<T> extends IJavaReflection, IJavaTyped, IJavaAnnotated {
	@Override
	public IJavaMemberType getType();
	
	public IJavaConcreteReflection<T> getDeclaringClass();
}
