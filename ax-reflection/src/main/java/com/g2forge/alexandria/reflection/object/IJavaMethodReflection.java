package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;
import com.g2forge.alexandria.java.reflect.IJavaAccessorMethod;
import com.g2forge.alexandria.java.reflect.JavaAccessorMethod;

public interface IJavaMethodReflection<T, O> extends IJavaMemberReflection<T> {
	public IJavaTypeReflection<O> getReturnType();

	@Override
	public IJavaMethodType getType();

	public O invoke(T object, Object... args);

	public default IJavaAccessorMethod toAccessorMethod() {
		return new JavaAccessorMethod(IJavaMethodReflection.this.getType().getJavaMember());
	}
}
