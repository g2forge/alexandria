package com.g2forge.alexandria.reflection.typed;

import com.g2forge.alexandria.java.typed.IGenericTyped;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;

public interface IReflectionGenericTyped<T> extends IReflectionTyped, IGenericTyped<T, IJavaTypeReflection<?>> {
	public IJavaTypeReflection<T> getType();
}
