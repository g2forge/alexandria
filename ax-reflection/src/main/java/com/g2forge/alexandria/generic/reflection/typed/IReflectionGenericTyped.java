package com.g2forge.alexandria.generic.reflection.typed;

import com.g2forge.alexandria.generic.java.typed.IGenericTyped;
import com.g2forge.alexandria.generic.reflection.object.IJavaTypeReflection;

public interface IReflectionGenericTyped<T> extends IReflectionTyped, IGenericTyped<T, IJavaTypeReflection<?>> {
	public IJavaTypeReflection<T> getType();
}
