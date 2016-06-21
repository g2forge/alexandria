package com.g2forge.alexandria.generic.type.java.implementations;

import java.lang.reflect.Constructor;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaMemberType;
import com.g2forge.alexandria.generic.type.java.IJavaConstructorType;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;

public class JavaConstructorType extends AJavaMemberType<Constructor<?>>implements IJavaConstructorType {
	public JavaConstructorType(final Constructor<?> constructor, final ITypeEnvironment environment) {
		super(constructor, environment);
	}

	@Override
	public IJavaUntype eval(ITypeEnvironment environment) {
		return new JavaConstructorType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}
}
