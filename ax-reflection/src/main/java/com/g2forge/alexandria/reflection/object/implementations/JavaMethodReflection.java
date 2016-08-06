package com.g2forge.alexandria.reflection.object.implementations;

import java.lang.reflect.InvocationTargetException;

import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;
import com.g2forge.alexandria.reflection.object.ReflectionHelpers;

public class JavaMethodReflection<T, O> extends AJavaMemberReflection<T, IJavaMethodType>implements IJavaMethodReflection<T, O> {
	public JavaMethodReflection(IJavaMethodType type) {
		super(type);
	}

	@Override
	public IJavaTypeReflection<O> getReturnType() {
		return ReflectionHelpers.toReflection(getType().getJavaMember().getGenericReturnType());
	}

	@Override
	public O invoke(T object, Object... args) {
		try {
			@SuppressWarnings("unchecked")
			final O retVal = (O) type.getJavaMember().invoke(object, args);
			return retVal;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}
}
