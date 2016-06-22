package com.g2forge.alexandria.generic.type.java.implementations;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaMemberType;
import com.g2forge.alexandria.generic.type.java.IJavaConstructorType;
import com.g2forge.alexandria.generic.type.java.IJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.java.function.CachingSupplier;

public class JavaConstructorType extends AJavaMemberType<Constructor<?>>implements IJavaConstructorType {
	public JavaConstructorType(final Constructor<?> constructor, final ITypeEnvironment environment) {
		super(constructor, environment);
	}

	@Override
	public IJavaUntype eval(ITypeEnvironment environment) {
		return new JavaConstructorType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}

	protected final Supplier<List<IJavaType>> parameterTypes = new CachingSupplier<>(() -> Collections.unmodifiableList(Stream.of(getJavaMember().getGenericParameterTypes()).map(type -> JavaTypeHelpers.toType(type, environment)).collect(Collectors.toList())));

	@Override
	public List<IJavaType> getParameterTypes() {
		return parameterTypes.get();
	}
}
