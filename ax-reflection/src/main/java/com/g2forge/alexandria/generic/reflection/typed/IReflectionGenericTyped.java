package com.g2forge.alexandria.generic.reflection.typed;

import com.g2forge.alexandria.generic.reflection.object.IJavaTypeReflection;
import com.g2forge.alexandria.java.typed.IGenericTyped;

public interface IReflectionGenericTyped<T> extends IReflectionTyped, IGenericTyped<T, IJavaTypeReflection<?>> {
	public IJavaTypeReflection<T> getType();
}
