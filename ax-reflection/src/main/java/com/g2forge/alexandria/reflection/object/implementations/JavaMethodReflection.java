package com.g2forge.alexandria.reflection.object.implementations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.HReflection;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;

public class JavaMethodReflection<T, O> extends AJavaMemberReflection<T, IJavaMethodType>implements IJavaMethodReflection<T, O> {
	public JavaMethodReflection(IJavaMethodType type) {
		super(type);
	}

	@Override
	public IJavaTypeReflection<O> getReturnType() {
		return HReflection.toReflection(getType().getJavaMember().getGenericReturnType());
	}

	@Override
	public O invoke(T object, Object... args) {
		try {
			final Method method = type.getJavaMember();
			method.setAccessible(true);
			@SuppressWarnings("unchecked")
			final O retVal = (O) method.invoke(object, args);
			return retVal;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}
}
