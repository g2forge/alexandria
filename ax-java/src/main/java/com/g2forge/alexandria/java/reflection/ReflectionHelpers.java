package com.g2forge.alexandria.java.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.TreeHelpers;

public class ReflectionHelpers {
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
		return filter(scope, minimum, getClasses(klass, scope).map(Class::getDeclaredMethods).flatMap(Stream::of));
	}
}
