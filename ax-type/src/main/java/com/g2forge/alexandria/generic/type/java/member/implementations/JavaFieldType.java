package com.g2forge.alexandria.generic.type.java.member.implementations;

import java.lang.reflect.Field;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.HJavaType;
import com.g2forge.alexandria.generic.type.java.member.AJavaMemberType;
import com.g2forge.alexandria.generic.type.java.member.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;

public class JavaFieldType extends AJavaMemberType<Field>implements IJavaFieldType {
	public JavaFieldType(final Field field, final ITypeEnvironment environment) {
		super(field, environment);
	}

	@Override
	public IJavaFieldType eval(ITypeEnvironment environment) {
		return new JavaFieldType(javaType, TypeEnvironment.create(null, this.environment, EmptyTypeEnvironment.create(environment)));
	}

	@Override
	public IJavaType getFieldType() {
		return HJavaType.toType(javaType.getGenericType(), environment);
	}
}
