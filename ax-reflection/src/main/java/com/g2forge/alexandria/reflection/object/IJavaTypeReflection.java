package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.structure.IJavaTypeStructure;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;

public interface IJavaTypeReflection<T> extends IJavaGenericTyped<T>, IJavaTypeStructure<IJavaClassReflection<?>> {
	@Override
	public IJavaType getType();
	
	@Override
	public IJavaClassReflection<? super T> erase();
}
