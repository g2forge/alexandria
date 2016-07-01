package com.g2forge.alexandria.reflection.object;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import com.g2forge.alexandria.adt.collection.strategy.ICollectionStrategy;
import com.g2forge.alexandria.adt.collection.strategy.implementations.CollectionStrategy;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.reflection.object.implementations.JavaConcreteReflection;
import com.g2forge.alexandria.reflection.object.implementations.JavaTypeReflection;
import com.g2forge.alexandria.reflection.typed.IReflectionGenericTyped;

public class ReflectionHelpers {
	@SuppressWarnings("unchecked")
	public static <C extends Collection<T>, T> ICollectionStrategy<C, T> create(IReflectionGenericTyped<C> typed) {
		if (Collection.class.equals(typed.getType().erase().getType().getJavaType())) { return (ICollectionStrategy<C, T>) new CollectionStrategy<T>(); }
		throw new IllegalArgumentException("Collection type \"" + typed.getType() + "\" is not recognized, so we can't help you here (sorry)!");
	}

	public static <T> IJavaConcreteReflection<T> toReflection(final Class<T> type) {
		return toReflection(type, EmptyTypeEnvironment.create());
	}

	public static <T> IJavaConcreteReflection<T> toReflection(final Class<T> type, final ITypeEnvironment environment) {
		return new JavaConcreteReflection<>(type, environment);
	}

	@SuppressWarnings("unchecked")
	public static <T> IJavaConcreteReflection<T> toReflection(T value) {
		return (IJavaConcreteReflection<T>) toReflection(value.getClass(), EmptyTypeEnvironment.create());
	}

	public static <T> IJavaTypeReflection<T> toReflection(final Type type) {
		return toReflection(type, EmptyTypeEnvironment.create());
	}

	public static <T> IJavaTypeReflection<T> toReflection(final Type type, final ITypeEnvironment environment) {
		if (type == null) return null;
		if (type instanceof Class) {
			@SuppressWarnings("unchecked")
			final Class<T> klass = (Class<T>) type;
			return toReflection(klass, environment);
		}
		if (type instanceof ParameterizedType) {
			final ParameterizedType parameterized = (ParameterizedType) type;
			JavaTypeHelpers.toType(parameterized, null).toEnvironment();
			return toReflection(parameterized.getRawType(), JavaTypeHelpers.toType(parameterized, null).toEnvironment());
		}
		return new JavaTypeReflection<T>(type, environment);
	}
}
