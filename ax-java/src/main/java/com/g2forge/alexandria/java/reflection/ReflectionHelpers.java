package com.g2forge.alexandria.java.reflection;

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
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.MapHelpers;
import com.g2forge.alexandria.java.TreeHelpers;

public class ReflectionHelpers {
	protected static class OverrideFilter implements Predicate<Method> {
		protected final Map<String, Collection<Method>> map = new HashMap<>();

		@Override
		public boolean test(Method method) {
			final Collection<Method> methods = MapHelpers.createOrGet(map, method.getName(), name -> new ArrayList<>());
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

	protected static <T extends Member> Stream<T> filter(JavaScope scope, JavaProtection minimum, final Stream<T> members) {
		final Stream<T> scoped = members.filter(member -> !(scope.isStatics() ^ Modifier.isStatic(member.getModifiers())));
		final Stream<T> filtered = minimum == null ? scoped : scoped.filter(member -> JavaProtection.of(member).compareTo(minimum) >= 0);
		return filtered;
	}

	protected static Stream<Class<?>> getClasses(Class<?> klass, JavaScope scope) {
		return scope.isInherited() ? TreeHelpers.<Class<?>>dfs(klass, c -> Object.class.equals(c) ? Collections.emptyList() : Arrays.<Class<?>>asList(c.getSuperclass()), true) : Stream.of(klass);
	}

	public static Stream<Field> getFields(Class<?> klass, JavaScope scope, JavaProtection minimum) {
		return filter(scope, minimum, getClasses(klass, scope).map(Class::getDeclaredFields).flatMap(Stream::of));
	}

	public static Stream<Method> getMethods(Class<?> klass, JavaScope scope, JavaProtection minimum) {
		final Stream<Method> basic = filter(scope, minimum, getClasses(klass, scope).map(Class::getDeclaredMethods).flatMap(Stream::of));
		return basic.sequential().filter(new OverrideFilter());
	}
}
