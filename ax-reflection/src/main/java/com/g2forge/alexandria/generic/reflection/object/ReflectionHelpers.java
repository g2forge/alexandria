package com.g2forge.alexandria.generic.reflection.object;

import java.lang.reflect.Type;
import java.util.Collection;

import com.g2forge.alexandria.adt.collection.strategy.ICollectionStrategy;
import com.g2forge.alexandria.adt.collection.strategy.implementations.CollectionStrategy;
import com.g2forge.alexandria.generic.reflection.object.implementations.JavaClassReflection;
import com.g2forge.alexandria.generic.reflection.object.implementations.JavaTypeReflection;
import com.g2forge.alexandria.generic.reflection.typed.IReflectionGenericTyped;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public class ReflectionHelpers {
	public static <T> IJavaClassReflection<T> toReflection(final Class<T> type, final ITypeEnvironment environment) {
		return new JavaClassReflection<>(type, environment);
	}
	
	public static <T> IJavaTypeReflection<T> toReflection(final Type type, final ITypeEnvironment environment) {
		if (type == null) return null;
		if (type instanceof Class) {
			@SuppressWarnings("unchecked") final Class<T> xclass = (Class<T>) type;
			return new JavaClassReflection<T>(xclass, environment);
		}
		return new JavaTypeReflection<T>(type, environment);
	}
	
	@SuppressWarnings("unchecked")
	public static <C extends Collection<T>, T> ICollectionStrategy<C, T> create(IReflectionGenericTyped<C> typed) {
		if (Collection.class.equals(typed.getType().erase().getType().getJavaType())) { return (ICollectionStrategy<C, T>) new CollectionStrategy<T>(); }
		throw new IllegalArgumentException("Collection type \"" + typed.getType() + "\" is not recognized, so we can't help you here (sorry)!");
	}
}
