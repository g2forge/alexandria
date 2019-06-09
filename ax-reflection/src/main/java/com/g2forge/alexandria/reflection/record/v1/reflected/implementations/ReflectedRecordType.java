package com.g2forge.alexandria.reflection.record.v1.reflected.implementations;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.reflection.object.IJavaConcreteReflection;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.object.implementations.JavaConcreteReflection;
import com.g2forge.alexandria.reflection.record.v1.reflected.IReflectedFieldType;
import com.g2forge.alexandria.reflection.record.v1.reflected.IReflectedRecordType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ReflectedRecordType<R> implements IReflectedRecordType<R> {
	@SuppressWarnings("unchecked" /* TODO: Should be able to use a ::new method reference on type inference gets better again */)
	protected static <R> ReflectedFieldType<R, Object> field(IJavaFieldReflection<R, ?> field) {
		return new ReflectedFieldType<R, Object>((IJavaFieldReflection<R, Object>) field);
	}

	@Getter(AccessLevel.PROTECTED)
	protected final IJavaConcreteReflection<R> reflection;

	@Getter(lazy = true)
	private final Collection<? extends IReflectedFieldType<R, ?>> fields = Collections.unmodifiableCollection(getReflection().getFields(JavaScope.Inherited, null).map(ReflectedRecordType::field).collect(Collectors.toList()));

	public ReflectedRecordType(final Class<R> type, final ITypeEnvironment environment) {
		this(new JavaConcreteReflection<R>(type, environment));
	}

	@Override
	public R get() {
		return getReflection().newInstance();
	}

	@Override
	public IJavaConcreteType getType() {
		return getReflection().getType();
	}
}
