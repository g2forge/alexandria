package com.g2forge.alexandria.reflection.typed;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;
import com.g2forge.alexandria.reflection.object.ReflectionHelpers;

import lombok.Getter;

public abstract class ATypeReference<T> implements ITypeReference<T> {
	@Getter
	protected final IJavaTypeReflection<T> type;

	protected ATypeReference() {
		final Type superClass = getClass().getGenericSuperclass();
		if (!(superClass instanceof ParameterizedType)) throw new IllegalArgumentException("Type reference constructed without any concrete type!");
		final ParameterizedType parameterizedType = (ParameterizedType) superClass;
		if (parameterizedType.getActualTypeArguments().length != 1) throw new IllegalArgumentException("Type reference constructed with the wrong number of type arguments!");
		type = ReflectionHelpers.toReflection(parameterizedType.getActualTypeArguments()[0]);
	}
}
