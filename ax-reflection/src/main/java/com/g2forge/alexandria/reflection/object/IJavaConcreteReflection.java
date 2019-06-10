package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.structure.IJavaClassStructure;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;
import com.g2forge.alexandria.metadata.v5.annotation.IJavaAnnotated;

public interface IJavaConcreteReflection<T> extends IJavaGenericTyped<T>, IJavaAnnotated, IJavaClassStructure<IJavaConcreteReflection<?>, IJavaTypeReflection<?>, IJavaFieldReflection<T, ?>, IJavaMethodReflection<T, ?>, IJavaConstructorReflection<T>>, IJavaTypeReflection<T> {
	@Override
	public IJavaConcreteType getType();

	public T newInstance();

	public IJavaConcreteReflection<T> toNonPrimitive();
}
