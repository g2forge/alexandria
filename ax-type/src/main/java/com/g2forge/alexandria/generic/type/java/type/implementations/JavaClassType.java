package com.g2forge.alexandria.generic.type.java.type.implementations;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.HJavaType;
import com.g2forge.alexandria.generic.type.java.type.AJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.type.IJavaVariableType;
import com.g2forge.alexandria.java.core.error.UnreachableCodeError;
import com.g2forge.alexandria.java.core.helpers.HArray;

public class JavaClassType extends AJavaConcreteType<Class<?>>implements IJavaClassType {
	public JavaClassType(final Class<?> javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}

	@Override
	public IJavaConcreteType bind(final List<? extends IType> actuals) {
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
		return new JavaClassType(javaType, TypeEnvironment.create(null, this.environment, EmptyTypeEnvironment.create(environment)));
	}

	@Override
	public List<? extends IJavaType> getActuals() {
		return getParameters().stream().map(parameter -> parameter.eval(environment)).collect(Collectors.toList());
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
	public IJavaType getOwner() {
		return HJavaType.toType(javaType.getEnclosingClass(), environment);
	}

	@Override
	public List<? extends IJavaVariableType> getParameters() {
		return HArray.map(input -> new JavaVariableType(input, environment), javaType.getTypeParameters());
	}

	protected IJavaClassType getParent(final Type generic, final Class<?> parent) {
		if (generic == null) return null;
		final ITypeEnvironment environment = ((IJavaConcreteType) HJavaType.toType(generic, this.environment)).toEnvironment();
		return new JavaClassType(parent, environment);
	}

	@Override
	public IJavaType getRaw() {
		return this;
	}

	@Override
	public IJavaClassType getSuperClass() {
		return getParent(javaType.getGenericSuperclass(), javaType.getSuperclass());
	}

	@Override
	public boolean isEnum() {
		return javaType.isEnum();
	}

	@Override
	public ITypeEnvironment toEnvironment() {
		return EmptyTypeEnvironment.create();
	}

	@Override
	public IJavaConcreteType toNonPrimitive() {
		if ((javaType instanceof Class) && ((Class<?>) javaType).isPrimitive()) {
			final Class<?> retVal;
			if (Boolean.TYPE.equals(javaType)) retVal = Boolean.class;
			else if (Character.TYPE.equals(javaType)) retVal = Character.class;
			else if (Byte.TYPE.equals(javaType)) retVal = Byte.class;
			else if (Short.TYPE.equals(javaType)) retVal = Short.class;
			else if (Integer.TYPE.equals(javaType)) retVal = Integer.class;
			else if (Long.TYPE.equals(javaType)) retVal = Long.class;
			else if (Float.TYPE.equals(javaType)) retVal = Float.class;
			else if (Double.TYPE.equals(javaType)) retVal = Double.class;
			else if (Void.TYPE.equals(javaType)) retVal = Void.class;
			else throw new UnreachableCodeError();
			return new JavaClassType(retVal, environment);
		}
		return this;
	}
}
