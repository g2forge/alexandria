package com.g2forge.alexandria.reflection.object.implementations;

import java.lang.reflect.InvocationTargetException;

import com.g2forge.alexandria.generic.type.java.IJavaConstructorType;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.IJavaConstructorReflection;

public class JavaConstructorReflection<O> extends AJavaMemberReflection<O, IJavaConstructorType>implements IJavaConstructorReflection<O> {
	public JavaConstructorReflection(IJavaConstructorType type) {
		super(type);
	}

	@Override
	public O newInstance(Object... args) {
		try {
			@SuppressWarnings("unchecked")
			O retVal = (O) type.getJavaMember().newInstance(args);
			return retVal;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}
}
