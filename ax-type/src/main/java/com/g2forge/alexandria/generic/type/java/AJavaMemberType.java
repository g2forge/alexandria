package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Member;
import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public abstract class AJavaMemberType<M extends Member> extends AJavaUntype<M>implements IJavaMemberType {
	public AJavaMemberType(final M member, final ITypeEnvironment environment) {
		super(member, environment);
	}

	@Override
	public IJavaClassType getDeclaringClass() {
		return JavaTypeHelpers.toType(getJavaMember().getDeclaringClass(), environment);
	}

	@Override
	public M getJavaMember() {
		return javaType;
	}

	@Override
	public Type getJavaType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return javaType.getName();
	}
}
