package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Executable;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaInvocableType;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.java.function.cache.CachingSupplier;

public abstract class AJavaInvocableType<M extends Executable> extends AJavaMemberType<M>implements IJavaInvocableType {
	protected final Supplier<List<IJavaType>> parameterTypes = new CachingSupplier<>(() -> Collections.unmodifiableList(Stream.of(getJavaMember().getGenericParameterTypes()).map(type -> JavaTypeHelpers.toType(type, environment)).collect(Collectors.toList())));

	public AJavaInvocableType(final M member, final ITypeEnvironment environment) {
		super(member, environment);
	}

	@Override
	public List<IJavaType> getParameterTypes() {
		return parameterTypes.get();
	}
}
