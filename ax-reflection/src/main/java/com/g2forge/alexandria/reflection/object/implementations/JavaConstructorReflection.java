package com.g2forge.alexandria.reflection.object.implementations;

import java.lang.reflect.InvocationTargetException;

import com.g2forge.alexandria.generic.type.java.member.IJavaConstructorType;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.IJavaConstructorReflection;

public class JavaConstructorReflection<T> extends AJavaMemberReflection<T, IJavaConstructorType>implements IJavaConstructorReflection<T> {
	public JavaConstructorReflection(IJavaConstructorType type) {
		super(type);
	}

	@Override
	public T newInstance(Object... args) {
		try {
			@SuppressWarnings("unchecked")
			final T retVal = (T) type.getJavaMember().newInstance(args);
			return retVal;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}
}
