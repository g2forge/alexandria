package com.g2forge.alexandria.generic.type.java.type.implementations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.member.IJavaConstructorType;
import com.g2forge.alexandria.generic.type.java.member.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;
import com.g2forge.alexandria.generic.type.java.member.implementations.JavaConstructorType;
import com.g2forge.alexandria.generic.type.java.member.implementations.JavaFieldType;
import com.g2forge.alexandria.generic.type.java.member.implementations.JavaMethodType;
import com.g2forge.alexandria.generic.type.java.structure.JavaProtection;
import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.generic.type.java.structure.JavaStructureAnalyzer;
import com.g2forge.alexandria.generic.type.java.type.AJavaType;
import com.g2forge.alexandria.generic.type.java.type.IJavaBoundType;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.type.IJavaVariableType;
import com.g2forge.alexandria.java.associative.cache.Cache;
import com.g2forge.alexandria.java.associative.cache.NeverCacheEvictionPolicy;
import com.g2forge.alexandria.java.core.helpers.ArrayHelpers;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.Tuple2G_;

public class JavaClassType extends AJavaType<Class<?>>implements IJavaClassType {
	protected static final JavaStructureAnalyzer<IJavaType, IJavaFieldType, IJavaMethodType> analyzer;

	static {
		final Predicate<? super IJavaType> object = klass -> Object.class.equals(klass.getJavaType());
		final Function<? super IJavaType, ? extends IJavaType> superclass = IJavaType::getSuperClass;
		final Function<? super IJavaType, ? extends Stream<? extends IJavaMethodType>> methods = klass -> {
			final JavaClassType cast = (JavaClassType) klass;
			return Stream.of(cast.javaType.getDeclaredMethods()).map(m -> new JavaMethodType(m, cast.environment));
		};
		final Function<? super IJavaMethodType, ? extends Method> method = IJavaMethodType::getJavaMember;
		final Function<? super IJavaType, ? extends Stream<? extends IJavaFieldType>> fields = klass -> {
			final JavaClassType cast = (JavaClassType) klass;
			return Stream.of(cast.javaType.getDeclaredFields()).map(f -> new JavaFieldType(f, cast.environment));
		};
		final Function<? super IJavaFieldType, ? extends Field> field = IJavaFieldType::getJavaMember;
		analyzer = new JavaStructureAnalyzer<>(object, superclass, methods, method, fields, field);
	}

	protected final Function<ITuple2G_<JavaScope, JavaProtection>, List<IJavaFieldType>> fields = new Cache<>(args -> analyzer.getFields(this, args.get0(), args.get1()).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

	protected final Function<ITuple2G_<JavaScope, JavaProtection>, List<IJavaMethodType>> methods = new Cache<>(args -> analyzer.getMethods(this, args.get0(), args.get1()).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

	protected final Function<JavaProtection, List<IJavaConstructorType>> constructors = new Cache<JavaProtection, List<IJavaConstructorType>>(minimum -> Stream.of(javaType.getConstructors()).filter(new JavaStructureAnalyzer.ProtectionFilter<>(Function.identity(), minimum)).map(constructor -> new JavaConstructorType(constructor, environment)).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

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

	@Override
	public Stream<? extends IJavaConstructorType> getConstructors(JavaProtection minimum) {
		return constructors.apply(minimum).stream();
	}

	public Stream<IJavaFieldType> getFields(JavaScope scope, JavaProtection minimum) {
		return fields.apply(new Tuple2G_<>(scope, minimum)).stream();
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

	public Stream<IJavaMethodType> getMethods(JavaScope scope, JavaProtection minimum) {
		return methods.apply(new Tuple2G_<>(scope, minimum)).stream();
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
