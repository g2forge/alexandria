package com.g2forge.alexandria.generic.type.java.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
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
import com.g2forge.alexandria.generic.type.java.type.implementations.JavaClassType;
import com.g2forge.alexandria.java.associative.cache.Cache;
import com.g2forge.alexandria.java.associative.cache.NeverCacheEvictionPolicy;
import com.g2forge.alexandria.java.core.error.UnreachableCodeError;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.implementations.Tuple2G_O;

public abstract class AJavaConcreteType<JT extends Type> extends AJavaType<JT>implements IJavaConcreteType {
	protected static final JavaStructureAnalyzer<IJavaConcreteType, IJavaFieldType, IJavaMethodType> analyzer;

	static {
		final Predicate<? super IJavaConcreteType> object = klass -> Object.class.equals(klass.getJavaType());
		final Function<? super IJavaConcreteType, ? extends IJavaConcreteType> superclass = IJavaConcreteType::getSuperClass;
		final Function<? super IJavaConcreteType, ? extends Stream<? extends IJavaMethodType>> methods = klass -> {
			final JavaClassType cast = (JavaClassType) klass.erase();
			final ITypeEnvironment environment = TypeEnvironment.create(null, cast.environment, klass.toEnvironment());
			return Stream.of(cast.javaType.getDeclaredMethods()).map(m -> new JavaMethodType(m, environment));
		};
		final Function<? super IJavaMethodType, ? extends Method> method = IJavaMethodType::getJavaMember;
		final Function<? super IJavaConcreteType, ? extends Stream<? extends IJavaFieldType>> fields = klass -> {
			final JavaClassType cast = (JavaClassType) klass.erase();
			final ITypeEnvironment environment = TypeEnvironment.create(null, cast.environment, klass.toEnvironment());
			return Stream.of(cast.javaType.getDeclaredFields()).map(f -> new JavaFieldType(f, environment));
		};
		final Function<? super IJavaFieldType, ? extends Field> field = IJavaFieldType::getJavaMember;
		analyzer = new JavaStructureAnalyzer<>(object, superclass, methods, method, fields, field);
	}

	protected final Function<ITuple2G_<JavaScope, JavaProtection>, List<IJavaFieldType>> fields = new Cache<>(args -> analyzer.getFields(this, args.get0(), args.get1()).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

	protected final Function<ITuple2G_<JavaScope, JavaProtection>, List<IJavaMethodType>> methods = new Cache<>(args -> analyzer.getMethods(this, args.get0(), args.get1()).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

	protected final Function<JavaProtection, List<IJavaConstructorType>> constructors = new Cache<JavaProtection, List<IJavaConstructorType>>(minimum -> {
		final JavaClassType cast = (JavaClassType) erase();
		return Stream.of(cast.javaType.getConstructors()).filter(new JavaStructureAnalyzer.ProtectionFilter<>(Function.identity(), minimum)).map(constructor -> new JavaConstructorType(constructor, environment)).collect(Collectors.toList());
	} , NeverCacheEvictionPolicy.create());

	public AJavaConcreteType(final JT javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}

	@Override
	public Stream<? extends IJavaConstructorType> getConstructors(JavaProtection minimum) {
		return constructors.apply(minimum).stream();
	}

	public Stream<IJavaFieldType> getFields(JavaScope scope, JavaProtection minimum) {
		return fields.apply(new Tuple2G_O<>(scope, minimum)).stream();
	}

	@Override
	public Stream<? extends IJavaConcreteType> getInterfaces() {
		final ITypeEnvironment environment = toEnvironment();
		return erase().getInterfaces().map(i -> i.eval(environment));
	}

	public Stream<IJavaMethodType> getMethods(JavaScope scope, JavaProtection minimum) {
		return methods.apply(new Tuple2G_O<>(scope, minimum)).stream();
	}

	@Override
	public IJavaConcreteType getParent(IJavaConcreteType parent) {
		final Class<?> rawThis = (Class<?>) erase().getJavaType();
		final Class<?> rawParent = (Class<?>) parent.erase().getJavaType();
		if (!rawParent.isAssignableFrom(rawThis)) throw new IllegalArgumentException();
		if (rawThis.equals(rawParent)) return this;

		final Function<IJavaConcreteType, IJavaConcreteType> function = type -> {
			if (type == null) return null;
			try {
				final IJavaConcreteType retVal = type.getParent(parent);
				return retVal != null ? retVal.eval(environment) : null;
			} catch (IllegalArgumentException e) {
				return null;
			}
		};

		{
			final IJavaConcreteType retVal = function.apply(getSuperClass());
			if (retVal != null) return retVal;
		}
		for (IJavaConcreteType i : getInterfaces().collect(Collectors.toList())) {
			final IJavaConcreteType retVal = function.apply(i);
			if (retVal != null) return retVal;
		}

		throw new UnreachableCodeError();
	}

	@Override
	public IJavaConcreteType getSuperClass() {
		final IJavaConcreteType superclass = erase().getSuperClass();
		if (superclass == null) return null;
		return superclass.eval(toEnvironment());
	}

	@Override
	public IJavaConcreteType resolve() {
		return this;
	}
}
