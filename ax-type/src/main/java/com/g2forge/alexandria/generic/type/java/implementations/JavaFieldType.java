package com.g2forge.alexandria.generic.type.java.implementations;

import java.lang.reflect.Field;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaMemberType;
import com.g2forge.alexandria.generic.type.java.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.IJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;

public class JavaFieldType extends AJavaMemberType<Field>implements IJavaFieldType {
	public JavaFieldType(final Field field, final ITypeEnvironment environment) {
		super(field, environment);
	}

	@Override
	public IJavaUntype eval(ITypeEnvironment environment) {
		return new JavaFieldType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}

	@Override
	public IJavaType getType() {
		return JavaTypeHelpers.toType(javaType.getGenericType(), environment);
	}
}
