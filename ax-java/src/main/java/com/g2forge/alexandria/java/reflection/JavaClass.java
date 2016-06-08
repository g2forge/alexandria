package com.g2forge.alexandria.java.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.associative.cache.Cache;
import com.g2forge.alexandria.java.associative.cache.NeverCacheEvictionPolicy;
import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.Tuple2G_;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JavaClass<T> {
	protected final Class<T> inner;

	protected final Function<ITuple2G_<JavaScope, JavaProtection>, List<Field>> fields = new Cache<>(args -> ReflectionHelpers.getFields(getInner(), args.get0(), args.get1()).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

	protected final Function<ITuple2G_<JavaScope, JavaProtection>, List<Method>> methods = new Cache<>(args -> ReflectionHelpers.getMethods(getInner(), args.get0(), args.get1()).collect(Collectors.toList()), NeverCacheEvictionPolicy.create());

	public Stream<Field> getFields(JavaScope scope, JavaProtection minimum) {
		return fields.apply(new Tuple2G_<>(scope, minimum)).stream();
	}

	public Class<T> getInner() {
		return inner;
	}

	public Stream<Method> getMethods(JavaScope scope, JavaProtection minimum) {
		return methods.apply(new Tuple2G_<>(scope, minimum)).stream();
	}
}
