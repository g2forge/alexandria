package com.g2forge.alexandria.reflection.object.implementations;

import java.lang.reflect.InvocationTargetException;

import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;

public class JavaMethodReflection<O> extends AJavaMemberReflection<O, IJavaMethodType>implements IJavaMethodReflection<O> {
	public JavaMethodReflection(IJavaMethodType type) {
		super(type);
	}

	@Override
	public Object invoke(O object, Object... args) {
		try {
			return type.getJavaMember().invoke(object, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}
}
