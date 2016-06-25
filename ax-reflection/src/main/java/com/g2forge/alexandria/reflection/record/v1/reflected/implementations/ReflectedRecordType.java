package com.g2forge.alexandria.reflection.record.v1.reflected.implementations;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.java.function.ConcurrentCachingSupplier;
import com.g2forge.alexandria.reflection.object.IJavaClassReflection;
import com.g2forge.alexandria.reflection.object.implementations.JavaClassReflection;
import com.g2forge.alexandria.reflection.record.v1.reflected.IReflectedFieldType;
import com.g2forge.alexandria.reflection.record.v1.reflected.IReflectedRecordType;

import lombok.AccessLevel;
import lombok.Getter;

public class ReflectedRecordType<R> implements IReflectedRecordType<R> {
	@Getter(AccessLevel.PROTECTED)
	protected final IJavaClassReflection<R> reflection;

	protected final Supplier<Collection<IReflectedFieldType<R, ?>>> fields = new ConcurrentCachingSupplier<>(() -> Collections.unmodifiableCollection(getReflection().getFields(JavaScope.Inherited, null).map(field -> new ReflectedFieldType<>(field)).collect(Collectors.toList())));

	/**
	 * @param type
	 */
	public ReflectedRecordType(final Class<R> type, final ITypeEnvironment environment) {
		this(new JavaClassReflection<R>(type, environment));
	}

	/**
	 * @param reflection
	 */
	public ReflectedRecordType(IJavaClassReflection<R> reflection) {
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
	public IJavaClassType getType() {
		return getReflection().getType();
	}
}
