package com.g2forge.alexandria.reflection.object.implementations;

import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.reflection.object.AJavaTypeReflection;

public class JavaTypeReflection<T> extends AJavaTypeReflection<T, IJavaType> {
	public JavaTypeReflection(IJavaType type) {
		super(type);
	}

	public JavaTypeReflection(Type type, final ITypeEnvironment environment) {
		super(JavaTypeHelpers.toType(type, environment));
	}
}
