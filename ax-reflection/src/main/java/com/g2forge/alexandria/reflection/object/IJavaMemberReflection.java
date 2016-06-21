package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.IJavaMemberType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotated;

public interface IJavaMemberReflection<O, M> extends IJavaGenericTyped<M>, IJavaAnnotated {
	@Override
	public IJavaMemberType getType();
	
	public IJavaClassReflection<O> getDeclaringClass();
}
