package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.structure.IJavaClassStructure;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotated;

public interface IJavaClassReflection<T> extends IJavaGenericTyped<T>, IJavaAnnotated, IJavaClassStructure<IJavaClassReflection<?>, IJavaFieldReflection<T, ?>, IJavaMethodReflection<T>, IJavaConstructorReflection<T>>, IJavaTypeReflection<T> {
	@Override
	public IJavaClassType getType();

	public T newInstance();
}
