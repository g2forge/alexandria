package com.g2forge.alexandria.generic.reflection.object.implementations;

import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.reflection.object.AJavaTypeReflection;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaType;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;

public class JavaTypeReflection<T> extends AJavaTypeReflection<T, IJavaType> {
	/**
	 * @param type
	 */
	public JavaTypeReflection(IJavaType type) {
		super(type);
	}
	
	/**
	 * @param type
	 */
	public JavaTypeReflection(Type type, final ITypeEnvironment environment) {
		super(JavaTypeHelpers.toType(type, environment));
	}
}
