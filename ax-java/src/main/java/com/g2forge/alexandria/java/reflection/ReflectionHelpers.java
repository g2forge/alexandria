package com.g2forge.alexandria.java.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.TreeHelpers;

public class ReflectionHelpers {
	public static Stream<Field> getFields(Class<?> klass, JavaScope scope, JavaProtection minimum) {
		final Stream<Class<?>> classes = scope.isInherited() ? TreeHelpers.<Class<?>>dfs(klass, c -> Object.class.equals(c) ? Collections.emptyList() : Arrays.<Class<?>>asList(c.getSuperclass()), true) : Stream.of(klass);
		final Stream<Field> fields = classes.map(Class::getDeclaredFields).flatMap(Stream::of);
		final Stream<Field> scoped = fields.filter(field -> !(scope.isStatics() ^ Modifier.isStatic(field.getModifiers())));
		final Stream<Field> filtered = minimum == null ? scoped : scoped.filter(field -> JavaProtection.of(field).compareTo(minimum) >= 0);
		return filtered;
	}
}
