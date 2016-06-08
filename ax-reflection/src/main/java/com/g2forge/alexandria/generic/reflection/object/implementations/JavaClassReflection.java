package com.g2forge.alexandria.generic.reflection.object.implementations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import com.g2forge.alexandria.adt.collection.CollectionHelpers;
import com.g2forge.alexandria.generic.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.generic.reflection.annotations.implementations.JavaAnnotations;
import com.g2forge.alexandria.generic.reflection.object.AJavaTypeReflection;
import com.g2forge.alexandria.generic.reflection.object.IJavaClassReflection;
import com.g2forge.alexandria.generic.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.implementations.ReflectionException;
import com.g2forge.alexandria.generic.type.java.structure.JavaMembership;

public class JavaClassReflection<T> extends AJavaTypeReflection<T, IJavaClassType> implements IJavaClassReflection<T> {
	/**
	 * @param type
	 */
	public JavaClassReflection(Class<T> type, final ITypeEnvironment environment) {
		this(JavaTypeHelpers.toType(type, environment));
	}
	
	/**
	 * @param type
	 */
	public JavaClassReflection(IJavaClassType type) {
		super(type);
	}
	
	@Override
	public IJavaAnnotations getAnnotations() {
		return new JavaAnnotations(getInternal());
	}
	
	@Override
	public Collection<? extends IJavaFieldReflection<T, ?>> getFields(JavaMembership membership) {
		return CollectionHelpers.map(JavaFieldReflection::new, getType().getFields(membership));
	}
	
	@SuppressWarnings("unchecked")
	protected Class<T> getInternal() {
		return (Class<T>) type.getJavaType();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public IJavaClassReflection<?> getSuperClass() {
		return new JavaClassReflection(getType().getSuperClass());
	}
	
	@Override
	public IJavaClassType getType() {
		return type;
	}
	
	@Override
	public T newInstance() {
		try {
			final Constructor<T> constructor = getInternal().getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException exception) {
			throw new ReflectionException(exception);
		}
	}
}
