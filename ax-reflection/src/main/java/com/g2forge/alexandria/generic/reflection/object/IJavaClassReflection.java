package com.g2forge.alexandria.generic.reflection.object;

import com.g2forge.alexandria.generic.reflection.annotations.IJavaAnnotated;
import com.g2forge.alexandria.generic.type.java.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.structure.IJavaClassStructure;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;

public interface IJavaClassReflection<T> extends IJavaGenericTyped<T>, IJavaAnnotated, IJavaClassStructure<IJavaClassReflection<?>, IJavaFieldReflection<T, ?>>, IJavaTypeReflection<T> {
	@Override
	public IJavaClassType getType();
	
	public T newInstance();
}
