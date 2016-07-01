package com.g2forge.alexandria.generic.type.java.member.implementations;

import java.lang.reflect.Constructor;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.member.AJavaInvocableType;
import com.g2forge.alexandria.generic.type.java.member.IJavaConstructorType;

public class JavaConstructorType extends AJavaInvocableType<Constructor<?>>implements IJavaConstructorType {
	public JavaConstructorType(final Constructor<?> constructor, final ITypeEnvironment environment) {
		super(constructor, environment);
	}

	@Override
	public IJavaConstructorType eval(ITypeEnvironment environment) {
		return new JavaConstructorType(javaType, TypeEnvironment.create(null, this.environment, EmptyTypeEnvironment.create(environment)));
	}
}
