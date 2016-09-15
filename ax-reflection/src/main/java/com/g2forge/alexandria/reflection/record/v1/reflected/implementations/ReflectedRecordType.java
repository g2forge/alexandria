package com.g2forge.alexandria.reflection.record.v1.reflected.implementations;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.java.function.cache.ConcurrentLazySupplier;
import com.g2forge.alexandria.reflection.object.IJavaConcreteReflection;
import com.g2forge.alexandria.reflection.object.implementations.JavaConcreteReflection;
import com.g2forge.alexandria.reflection.record.v1.reflected.IReflectedFieldType;
import com.g2forge.alexandria.reflection.record.v1.reflected.IReflectedRecordType;

import lombok.AccessLevel;
import lombok.Getter;

public class ReflectedRecordType<R> implements IReflectedRecordType<R> {
	@Getter(AccessLevel.PROTECTED)
	protected final IJavaConcreteReflection<R> reflection;

	protected final Supplier<Collection<IReflectedFieldType<R, ?>>> fields = new ConcurrentLazySupplier<>(() -> Collections.unmodifiableCollection(getReflection().getFields(JavaScope.Inherited, null).map(field -> new ReflectedFieldType<>(field)).collect(Collectors.toList())));

	/**
	 * @param type
	 */
	public ReflectedRecordType(final Class<R> type, final ITypeEnvironment environment) {
		this(new JavaConcreteReflection<R>(type, environment));
	}

	/**
	 * @param reflection
	 */
	public ReflectedRecordType(IJavaConcreteReflection<R> reflection) {
		this.reflection = reflection;
	}

	@Override
	public R create() {
		return getReflection().newInstance();
	}

	@Override
	public Collection<? extends IReflectedFieldType<R, ?>> getFields() {
		return fields.get();
	}

	@Override
	public IJavaConcreteType getType() {
		return getReflection().getType();
	}
}
