package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.type.implementations.JavaBoundType;
import com.g2forge.alexandria.generic.type.java.type.implementations.JavaClassType;
import com.g2forge.alexandria.generic.type.java.type.implementations.JavaVariableType;
import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
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
