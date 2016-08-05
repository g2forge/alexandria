package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.structure.IJavaTypeStructure;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaGenericTyped;

public interface IJavaTypeReflection<T> extends IJavaReflection, IJavaGenericTyped<T>, IJavaTypeStructure<IJavaConcreteReflection<?>> {
	@Override
	public IJavaType getType();

	@Override
	public IJavaConcreteReflection<? super T> erase();
}
