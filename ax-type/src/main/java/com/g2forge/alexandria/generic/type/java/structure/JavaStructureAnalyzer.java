package com.g2forge.alexandria.generic.type.java.structure;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.g2forge.alexandria.adt.associative.map.HMap;
import com.g2forge.alexandria.java.core.helpers.HTree;
import com.g2forge.alexandria.java.reflect.JavaProtection;
import com.g2forge.alexandria.java.reflect.JavaScope;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JavaStructureAnalyzer<T, F, M> {
	protected class OverrideFilter implements Predicate<M> {
		protected final Map<String, Collection<Method>> map = new HashMap<>();

		@Override
		public boolean test(M m) {
			final Method method = JavaStructureAnalyzer.this.method.apply(m);

			final Collection<Method> methods = HMap.createOrGet(map, method.getName(), name -> new ArrayList<>());
			METHOD: for (Method prior : methods) {
				if (method.getParameterCount() != prior.getParameterCount()) continue;
				if (!prior.getReturnType().isAssignableFrom(method.getReturnType())) continue;
				for (int i = 0; i < method.getParameterCount(); i++) {
					if (!method.getParameterTypes()[i].isAssignableFrom(prior.getParameterTypes()[i])) continue METHOD;
				}
				return false;
			}
			methods.add(method);
			return true;
		}
	}

	@RequiredArgsConstructor
	public static class ProtectionFilter<M> implements Predicate<M> {
		protected final Function<? super M, ? extends Member> function;

		protected final JavaProtection minimum;

		@Override
		public boolean test(M member) {
			if (minimum == null) return true;
			return JavaProtection.of(function.apply(member)).compareTo(minimum) >= 0;
		}

	}

	public static final JavaStructureAnalyzer<Class<?>, Field, Method> REFLECTION_ANALYZER = new JavaStructureAnalyzer<Class<?>, Field, Method>(klass -> Object.class.equals(klass), Class::getSuperclass, c -> Stream.of(c.getDeclaredMethods()), Function.identity(), c -> Stream.of(c.getDeclaredFields()), Function.identity());

	protected static <T> Stream<T> filter(JavaScope scope, JavaProtection minimum, final Stream<T> members, Function<? super T, ? extends Member> function) {
		final Stream<T> scoped = members.filter(member -> !(scope.isStatics() ^ Modifier.isStatic(function.apply(member).getModifiers())));
		final Stream<T> filtered = minimum == null ? scoped : scoped.filter(new ProtectionFilter<>(function, minimum));
		return filtered;
	}

	protected final Predicate<? super T> object;

	protected final Function<? super T, ? extends T> superclass;

	protected final Function<? super T, ? extends Stream<? extends M>> methods;

	protected final Function<? super M, ? extends Method> method;

	protected final Function<? super T, ? extends Stream<? extends F>> fields;

	protected final Function<? super F, ? extends Field> field;

	public Stream<T> getClasses(T klass, JavaScope scope) {
		return scope.isInherited() ? HTree.<T>dfs(klass, c -> object.test(c) ? Collections.emptyList() : Arrays.asList(superclass.apply(c)), true) : Stream.of(klass);
	}

	public Stream<F> getFields(T klass, JavaScope scope, JavaProtection minimum) {
		return filter(scope, minimum, getClasses(klass, scope).map(fields).flatMap(Function.identity()), field);
	}

	public Stream<M> getMethods(T klass, JavaScope scope, JavaProtection minimum) {
		final Stream<M> basic = filter(scope, minimum, getClasses(klass, scope).map(methods).flatMap(Function.identity()), method);
		return basic.sequential().filter(new OverrideFilter());
	}
}
