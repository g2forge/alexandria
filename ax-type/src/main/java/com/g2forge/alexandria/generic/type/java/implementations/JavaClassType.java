package com.g2forge.alexandria.generic.type.java.implementations;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaBoundType;
import com.g2forge.alexandria.generic.type.java.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.IJavaMethodType;
import com.g2forge.alexandria.generic.type.java.IJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaTypeVariable;
import com.g2forge.alexandria.generic.type.java.JavaReflectionHelpers;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.structure.JavaProtection;
import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.java.associative.cache.Cache;
import com.g2forge.alexandria.java.associative.cache.NeverCacheEvictionPolicy;
import com.g2forge.alexandria.java.core.helpers.ArrayHelpers;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.Tuple2G_;

public class JavaClassType extends AJavaType<Class<?>>implements IJavaClassType {
	protected final Function<ITuple2G_<JavaScope, JavaProtection>, List<IJavaFieldType>> fields = new Cache<>(args -> JavaReflectionHelpers.getFields(javaType, args.get0(), args.get1()).map(input -> new JavaFieldType(input, environment)).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

	protected final Function<ITuple2G_<JavaScope, JavaProtection>, List<IJavaMethodType>> methods = new Cache<>(args -> JavaReflectionHelpers.getMethods(javaType, args.get0(), args.get1()).map(input -> new JavaMethodType(input, environment)).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

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
	public IJavaClassType erase() {
		return this;
	}

	@Override
	public IJavaClassType eval(final ITypeEnvironment environment) {
		return new JavaClassType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}

	public Stream<IJavaFieldType> getFields(JavaScope scope, JavaProtection minimum) {
		return fields.apply(new Tuple2G_<>(scope, minimum)).stream();
	}

	public Stream<IJavaMethodType> getMethods(JavaScope scope, JavaProtection minimum) {
		return methods.apply(new Tuple2G_<>(scope, minimum)).stream();
	}

	@Override
	public List<? extends IJavaTypeVariable> getParameters() {
		return ArrayHelpers.map(input -> new JavaVariableType(input, environment), javaType.getTypeParameters());
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
}
