package com.g2forge.alexandria.reflection.object;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;
import com.g2forge.alexandria.java.reflect.IJavaAccessorMethod;

public interface IJavaMethodReflection<T, O> extends IJavaMemberReflection<T> {
	public IJavaTypeReflection<O> getReturnType();

	@Override
	public IJavaMethodType getType();

	public O invoke(T object, Object... args);

	public default IJavaAccessorMethod toAccessorMethod() {
		return new IJavaAccessorMethod() {
			protected final Method method = IJavaMethodReflection.this.getType().getJavaMember();

			@Override
			public String getName() {
				return method.getName();
			}

			@Override
			public Type[] getParameterTypes() {
				return method.getGenericParameterTypes();
			}

			@Override
			public Type getReturnType() {
				return method.getGenericReturnType();
			}
		};
	}
}
