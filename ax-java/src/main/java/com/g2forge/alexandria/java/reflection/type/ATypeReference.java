package com.g2forge.alexandria.java.reflection.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.g2forge.alexandria.java.NotYetImplementedError;

public abstract class ATypeReference<T> implements ITypeReference<T> {
	protected static Class<?> erase(final Type type) throws NotYetImplementedError {
		if (type instanceof Class) return (Class<?>) type;
		if (type instanceof ParameterizedType) return erase(((ParameterizedType) type).getRawType());
		throw new NotYetImplementedError();
	}

	protected final Type type;

	protected ATypeReference() {
		final Type superclass = getClass().getGenericSuperclass();
		if (superclass instanceof Class<?>) throw new Error(ATypeReference.class.getSimpleName() + " was somehow constructed without type information!");
		type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? super T> getErased() {
		return (Class<? super T>) erase(getType());
	}

	@Override
	public Type getType() {
		return type;
	}
}
