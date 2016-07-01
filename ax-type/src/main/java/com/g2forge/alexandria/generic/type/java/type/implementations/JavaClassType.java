package com.g2forge.alexandria.generic.type.java.type.implementations;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.type.AJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaBoundType;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.type.IJavaVariableType;
import com.g2forge.alexandria.java.core.helpers.ArrayHelpers;

public class JavaClassType extends AJavaConcreteType<Class<?>>implements IJavaClassType {
	public JavaClassType(final Class<?> javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}

	@Override
	public IJavaBoundType bind(final List<? extends IType> actuals) {
		final Type[] actualArray = new Type[actuals.size()];
		for (int i = 0; i < actualArray.length; i++) {
			actualArray[i] = ((IJavaType) actuals.get(i)).getJavaType();
		}

		if (javaType.getTypeParameters().length != actualArray.length) throw new IllegalArgumentException("Wrong number of actual type arguments, expected " + javaType.getTypeParameters().length + ", but found " + actualArray.length + "!");
		return new JavaBoundType(new ParameterizedType() {
			@Override
			public Type[] getActualTypeArguments() {
				return actualArray;
			}

			@Override
			public Type getOwnerType() {
				return javaType.getEnclosingClass();
			}

			@Override
			public Type getRawType() {
				return javaType;
			}
		}, environment);
	}

	@Override
	public IJavaClassType erase() {
		return this;
	}

	@Override
	public IJavaClassType eval(final ITypeEnvironment environment) {
		return new JavaClassType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}

	@Override
	public Stream<? extends IJavaClassType> getInterfaces() {
		final Type[] generic = javaType.getGenericInterfaces();
		final Class<?>[] interfaces = javaType.getInterfaces();
		final IJavaClassType[] retVal = new IJavaClassType[generic.length];
		for (int i = 0; i < generic.length; i++)
			retVal[i] = getParent(generic[i], interfaces[i]);
		return Stream.of(retVal);
	}

	@Override
	public List<? extends IJavaVariableType> getParameters() {
		return ArrayHelpers.map(input -> new JavaVariableType(input, environment), javaType.getTypeParameters());
	}

	@Override
	public IJavaClassType getSuperClass() {
		return getParent(javaType.getGenericSuperclass(), javaType.getSuperclass());
	}

	@Override
	public boolean isEnum() {
		return javaType.isEnum();
	}
}
