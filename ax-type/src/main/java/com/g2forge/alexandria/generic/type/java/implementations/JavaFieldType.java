package com.g2forge.alexandria.generic.type.java.implementations;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaUntype;
import com.g2forge.alexandria.generic.type.java.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;

public class JavaFieldType extends AJavaUntype<Field> implements IJavaFieldType {
	/**
	 * @param field
	 * @param environment
	 */
	public JavaFieldType(final Field field, final ITypeEnvironment environment) {
		super(field, environment);
	}
	
	@Override
	public IJavaUntype eval(ITypeEnvironment environment) {
		return new JavaFieldType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}
	
	@Override
	public Field getJavaField() {
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
	
	@Override
	public IJavaUntype getType() {
		return JavaTypeHelpers.toType(javaType.getGenericType(), environment);
	}
}
