package com.g2forge.alexandria.generic.type.java.implementations;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.g2forge.alexandria.generic.java.map.MapHelpers;
import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaBoundType;
import com.g2forge.alexandria.generic.type.java.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.IJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaTypeVariable;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.structure.JavaMembership;

public class JavaClassType extends AJavaType<Class<?>>implements IJavaClassType {
	/**
	 * @param javaType
	 * @param environment
	 */
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
	public IJavaClassType eval(final ITypeEnvironment environment) {
		return new JavaClassType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}

	@Override
	public Collection<? extends IJavaFieldType> getFields(final JavaMembership membership) {
		final Collection<IJavaFieldType> retVal = MapHelpers.map(input -> new JavaFieldType(input, environment), javaType.getDeclaredFields());
		if ((membership == JavaMembership.All) && (javaType.getSuperclass() != null)) retVal.addAll(getSuperClass().getFields(membership));
		return retVal;
	}

	@Override
	public List<? extends IJavaTypeVariable> getParameters() {
		return MapHelpers.map(input -> new JavaVariableType(input, environment), javaType.getTypeParameters());
	}

	@Override
	public IJavaClassType getSuperClass() {
		final ITypeEnvironment environment = (this.environment != null) ? JavaTypeHelpers.toType(javaType.getGenericSuperclass(), this.environment).toEnvironment() : null;
		return new JavaClassType(javaType.getSuperclass(), environment);
	}

	@Override
	public boolean isEnum() {
		return javaType.isEnum();
	}

	@Override
	public IJavaClassType erase() {
		return this;
	}
}
