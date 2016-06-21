package com.g2forge.alexandria.generic.type.java.implementations;

import java.lang.reflect.Method;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaMemberType;
import com.g2forge.alexandria.generic.type.java.IJavaMethodType;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;

public class JavaMethodType extends AJavaMemberType<Method>implements IJavaMethodType {
	public JavaMethodType(final Method method, final ITypeEnvironment environment) {
		super(method, environment);
	}

	@Override
	public IJavaUntype eval(ITypeEnvironment environment) {
		return new JavaMethodType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}
}
