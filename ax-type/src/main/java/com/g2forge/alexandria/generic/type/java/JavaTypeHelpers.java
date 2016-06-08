package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.implementations.JavaBoundType;
import com.g2forge.alexandria.generic.type.java.implementations.JavaClassType;
import com.g2forge.alexandria.generic.type.java.implementations.JavaVariableType;

public class JavaTypeHelpers {
	public static IJavaClassType toType(final Class<?> type, final ITypeEnvironment environment) {
		return new JavaClassType(type, environment);
	}
	
	public static IJavaType toType(final Type type, final ITypeEnvironment environment) {
		if (type == null) return null;
		if (type instanceof Class) return new JavaClassType((Class<?>) type, environment);
		if (type instanceof TypeVariable) return new JavaVariableType((TypeVariable<?>) type, environment);
		if (type instanceof ParameterizedType) return new JavaBoundType((ParameterizedType) type, environment);
		throw new IllegalArgumentException("Type \"" + type + "\" is not a recognized java reflection type");
	}
}
