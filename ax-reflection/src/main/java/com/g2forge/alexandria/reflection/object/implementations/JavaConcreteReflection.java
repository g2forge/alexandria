package com.g2forge.alexandria.reflection.object.implementations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.structure.JavaProtection;
import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.implementations.ReflectionException;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.reflection.annotations.implementations.JavaAnnotations;
import com.g2forge.alexandria.reflection.object.AJavaTypeReflection;
import com.g2forge.alexandria.reflection.object.IJavaConcreteReflection;
import com.g2forge.alexandria.reflection.object.IJavaConstructorReflection;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;
import com.g2forge.alexandria.reflection.object.ReflectionHelpers;

public class JavaConcreteReflection<T> extends AJavaTypeReflection<T, IJavaConcreteType>implements IJavaConcreteReflection<T> {
	public JavaConcreteReflection(Class<T> type, final ITypeEnvironment environment) {
		this(JavaTypeHelpers.toType(type, environment));
	}

	public JavaConcreteReflection(IJavaConcreteType type) {
		super(type);
	}

	@Override
	public IJavaAnnotations getAnnotations() {
		return new JavaAnnotations(getInternal());
	}

	@Override
	public Stream<? extends IJavaConstructorReflection<T>> getConstructors(JavaProtection minimum) {
		return getType().getConstructors(minimum).map(JavaConstructorReflection::new);
	}

	@Override
	public Stream<? extends IJavaFieldReflection<T, ?>> getFields(JavaScope scope, JavaProtection minimum) {
		return getType().getFields(scope, minimum).map(JavaFieldReflection::new);
	}

	@Override
	public Stream<? extends IJavaConcreteReflection<?>> getInterfaces() {
		return getType().getInterfaces().map(JavaConcreteReflection::new);
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getInternal() {
		return (Class<T>) type.getJavaType();
	}

	@Override
	public Stream<? extends IJavaMethodReflection<T, ?>> getMethods(JavaScope scope, JavaProtection minimum) {
		return getType().getMethods(scope, minimum).map(JavaMethodReflection::new);
	}

	@Override
	public IJavaTypeReflection<?> getParent(IJavaTypeReflection<?> parent) {
		return ReflectionHelpers.toReflection(getType().getParent(parent.getType().resolve()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IJavaConcreteReflection<?> getSuperClass() {
		return new JavaConcreteReflection(getType().getSuperClass());
	}

	@Override
	public IJavaConcreteType getType() {
		return type;
	}

	@Override
	public boolean isEnum() {
		return getType().isEnum();
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

	@Override
	public IJavaConcreteReflection<T> toNonPrimitive() {
		return new JavaConcreteReflection<>(getType().toNonPrimitive());
	}
}
