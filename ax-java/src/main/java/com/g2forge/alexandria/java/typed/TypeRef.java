package com.g2forge.alexandria.java.typed;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.g2forge.alexandria.java.core.error.ReflectedCodeError;

import lombok.Getter;

public abstract class TypeRef<T> implements ITypeRef<T> {
	@Getter
	protected final Type type;

	protected TypeRef() {
		final Type superclass = getClass().getGenericSuperclass();
		if (!(superclass instanceof ParameterizedType)) throw new ReflectedCodeError("Super class is not a parameterized type!");
		final ParameterizedType parameterized = (ParameterizedType) superclass;
		if (!parameterized.getRawType().equals(TypeRef.class)) throw new ReflectedCodeError("Super class is not " + TypeRef.class + "!");
		type = parameterized.getActualTypeArguments()[0];
	}
}
